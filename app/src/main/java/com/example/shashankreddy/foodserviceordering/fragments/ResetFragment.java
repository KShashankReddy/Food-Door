package com.example.shashankreddy.foodserviceordering.fragments;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shashankreddy.foodserviceordering.R;
import com.example.shashankreddy.foodserviceordering.appController.AppController;
import com.example.shashankreddy.foodserviceordering.utils.PasswordValidator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ricky on 2017/1/16.
 */

public class ResetFragment extends Fragment {

    private final String TAG = "Reset Password";
    TextInputEditText oldPswd, newPswd, confirmPswd;
    Button submitBtn;
    String mobile, oldP, newP, confirmP;

    PasswordValidator validator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        validator = new PasswordValidator();
        Bundle bundle=getArguments();
        mobile=bundle.getString("mobile");
        oldP=bundle.getString("password");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_reset,container,false);

        oldPswd = (TextInputEditText) v.findViewById(R.id.oldPassword);
        newPswd = (TextInputEditText) v.findViewById(R.id.newPassword);
        confirmPswd = (TextInputEditText) v.findViewById(R.id.confirmPassword);
        submitBtn = (Button) v.findViewById(R.id.submitBtn);

        oldPswd.setText(oldP);

        submitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                oldP=oldPswd.getText().toString();
                newP=newPswd.getText().toString();
                confirmP=confirmPswd.getText().toString();
                if(!oldP.isEmpty() && !newP.isEmpty() && !confirmP.isEmpty())
                {
                    if(validator.validate(oldP) && validator.validate(newP) && validator.validate(confirmP))
                    {
                        if(newP.equals(confirmP))
                        {
                            checkPassword(mobile, oldP, newP);
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Inconsistent new password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        if(!validator.validate(oldP))
                            oldPswd.setError(validator.PASSWORD_DISCRIPTION);
                        if(!validator.validate(newP))
                            newPswd.setError(validator.PASSWORD_DISCRIPTION);
                        if(!validator.validate(confirmP))
                            confirmPswd.setError(validator.PASSWORD_DISCRIPTION);
                    }
                }
                else
                {
                    if(oldP.isEmpty())
                    {
                        oldPswd.setError("Please enter the provisional password");
                    }
                    if(newP.isEmpty())
                    {
                        newPswd.setError("Please enter the new password");
                    }
                    if(confirmP.isEmpty())
                    {
                        confirmPswd.setError("Please enter the confirm new password");
                    }
                }
            }
        });

        return v;
    }

    private void checkPassword(String mobile, String oldP, String newP)
    {
        String urlReset="http://rjtmobile.com/ansari/fos/fosapp/fos_reset_pass.php?&user_phone="+mobile+"&user_password="+oldP+"&newpassword="+newP;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, urlReset, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.d(TAG, response.toString());
                try
                {
                    JSONArray array = response.getJSONArray("msg");
                    String message=array.getString(0);
                    if(message.equals("password reset successfully"))
                    {
                        Toast.makeText(getActivity(),"Password reset success!",Toast.LENGTH_SHORT).show();
                        LoginFragment fragment = new LoginFragment();
                        FragmentManager fm=getFragmentManager();
                        FragmentTransaction ft=fm.beginTransaction();
                        ft.replace(R.id.fragment,fragment).commit();
                    }
                    else if(message.equals("old password mismatch"))
                    {
                        Toast.makeText(getActivity(),"Old password mismatch!\nPlease try again!",Toast.LENGTH_SHORT).show();
                    }
                    else if(message.equals("wrong mobile number"))
                    {
                        Toast.makeText(getActivity(),"Wrong mobile number!",Toast.LENGTH_SHORT).show();
                        LoginFragment fragment = new LoginFragment();
                        FragmentManager fm=getFragmentManager();
                        FragmentTransaction ft=fm.beginTransaction();
                        ft.replace(R.id.fragment,fragment).commit();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addRequestQueue(req);
    }
}
