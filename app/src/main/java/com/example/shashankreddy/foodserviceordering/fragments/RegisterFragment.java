package com.example.shashankreddy.foodserviceordering.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.shashankreddy.foodserviceordering.R;
import com.example.shashankreddy.foodserviceordering.appController.AppController;
import com.example.shashankreddy.foodserviceordering.utils.PasswordValidator;

/**
 * Created by Ricky on 2017/1/16.
 */

public class RegisterFragment extends Fragment {

    public static final String TAG = "Registration";
    TextInputEditText userName, userEmail, userMobile, userPassword, userAddress;
    String mobile, password;
    Button regButton;
    PasswordValidator validator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        validator = new PasswordValidator();
        Bundle bundle=getArguments();
        mobile=bundle.getString("mobile");
        password=bundle.getString("password");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_reset,container,false);

        userName = (TextInputEditText) v.findViewById(R.id.userName);
        userEmail = (TextInputEditText) v.findViewById(R.id.userEmail);
        userMobile = (TextInputEditText) v.findViewById(R.id.userMobile);
        userPassword = (TextInputEditText) v.findViewById(R.id.userPassword);
        userAddress = (TextInputEditText) v.findViewById(R.id.userAddress);
        regButton = (Button) v.findViewById(R.id.regButton);
        userMobile.setText(mobile);
        userPassword.setText(password);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String uName = userName.getText().toString();
                String uEmail = userEmail.getText().toString();
                String uMobile = userMobile.getText().toString();
                String uPassword = userPassword.getText().toString();
                String uAddress = userAddress.getText().toString();
                if( uName.isEmpty() || uEmail.isEmpty() || uMobile.isEmpty() || uPassword.isEmpty() || uAddress.isEmpty())
                {
                    if(uName.isEmpty())
                        userName.setError("Please enter your name");
                    if(uEmail.isEmpty())
                        userEmail.setError("Please enter your email");
                    if(uMobile.isEmpty())
                        userMobile.setError("Please enter your phone number");
                    if(uPassword.isEmpty())
                        userPassword.setError("Please enter your password");
                    if(uAddress.isEmpty())
                        userAddress.setError("Please enter your address");
                }
                else if(!validator.validate(uPassword))
                {
                    userPassword.setError(validator.PASSWORD_DISCRIPTION);
                }
                else
                    registerNewUser(uName,uEmail,uMobile,uPassword, uAddress);
            }
        });

        return v;
    }

    private void registerNewUser(String name, String email, String mobile, String password, String address)
    {
        String urlReg="http://rjtmobile.com/ansari/fos/fosapp/fos_reg.php?&user_name="+name
                +"&user_email="+email+"&user_phone="+mobile+"&user_password="+password+"&user_add="+address;
        StringRequest strReq = new StringRequest(Request.Method.GET, urlReg, new Response.Listener<String>() {

            @Override
            public void onResponse(String response)
            {
                Log.d(TAG, response);
                if(response.equals("successfully registered"))
                {
                    Toast.makeText(getActivity(),"Successfully registered!",Toast.LENGTH_SHORT).show();
                    LoginFragment fragment = new LoginFragment();
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    ft.replace(R.id.fragment,fragment).commit();
                }
                else
                {
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Attention!");
                    alertDialog.setMessage("Mobile Number already exsist!\nPlease login or try a new mobile number.");
                    alertDialog.setCancelable(true);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.cancel();
                        }
                    });
                    AlertDialog ad=alertDialog.create();
                    ad.show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        // Adding request to request queue
        AppController.getInstance().addRequestQueue(strReq);
    }
}
