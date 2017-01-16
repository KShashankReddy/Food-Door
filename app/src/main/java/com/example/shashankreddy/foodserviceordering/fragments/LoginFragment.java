package com.example.shashankreddy.foodserviceordering.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.shashankreddy.foodserviceordering.LoginActivity;
import com.example.shashankreddy.foodserviceordering.MainActivity;
import com.example.shashankreddy.foodserviceordering.R;
import com.example.shashankreddy.foodserviceordering.appController.AppController;
import com.example.shashankreddy.foodserviceordering.utils.PasswordValidator;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ricky on 2017/1/16.
 */

public class LoginFragment extends Fragment {

    private final String TAG = "Login";

    TextInputEditText mobile, password;
    Button signInBtn, signUpBtn, resetBtn;
    String mobileNum, passWord;

    PasswordValidator validator;

    LoginButton loginButton;
    CallbackManager callbackManager;

    private AdView mAdView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
        validator = new PasswordValidator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_login,container,false);

        // login part
        mobile= (TextInputEditText) v.findViewById(R.id.mobile);
        mobile.requestFocus();
        password= (TextInputEditText) v.findViewById(R.id.password);
        signInBtn= (Button) v.findViewById(R.id.signInBtn);
        signUpBtn= (Button) v.findViewById(R.id.signUpBtn);
        resetBtn= (Button) v.findViewById(R.id.reset);
        loginButton = (LoginButton) v.findViewById(R.id.login_button);
        // ads part
        mAdView = (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("C04B1BFFB0774708339BC273F8A43708")
                .build();
        mAdView.loadAd(adRequest);

        // navigate to login
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(submitForm())
                {
                    if(validator.validate(passWord))
                    {
                        Toast.makeText(getActivity(),"Checking...",Toast.LENGTH_LONG).show();
                        checkLogin(mobileNum, passWord);
                    }
                    else
                    {
                        password.setError(validator.PASSWORD_DISCRIPTION);
                    }
                }
                else
                {
                    checkValidation(mobileNum, passWord);
                }
            }
        });
        // navigate to registration activity
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(submitForm())
                {
                    if(validator.validate(passWord))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("mobile",mobileNum);
                        bundle.putString("password",passWord);
                        RegisterFragment fragment = new RegisterFragment();
                        fragment.setArguments(bundle);
                        FragmentManager fm=getFragmentManager();
                        FragmentTransaction ft=fm.beginTransaction();
                        ft.replace(R.id.fragment,fragment).commit();
                    }
                    else
                    {
                        password.setError(validator.PASSWORD_DISCRIPTION);
                    }
                }
                else
                {
                    checkValidation(mobileNum, passWord);
                }
            }
        });
        // go to reset password screen
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(submitForm())
                {
                    if(validator.validate(passWord))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("mobile",mobileNum);
                        bundle.putString("password",passWord);
                        ResetFragment fragment = new ResetFragment();
                        fragment.setArguments(bundle);
                        FragmentManager fm=getFragmentManager();
                        FragmentTransaction ft=fm.beginTransaction();
                        ft.replace(R.id.fragment,fragment).commit();
                    }
                    else
                    {
                        password.setError(validator.PASSWORD_DISCRIPTION);
                    }
                }
                else
                {
                    checkValidation(mobileNum, passWord);
                }
            }
        });
        // use facebook login
        loginButton.setReadPermissions(Arrays.asList("public_profile, email"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                Log.d("facebook login",loginResult.toString());
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),new GraphRequest.GraphJSONObjectCallback()
                        {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response)
                            {
                                try
                                {
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String email = object.getString("email");
                                    // store data
                                    SharedPreferences setting=getActivity().getSharedPreferences("Food@Door",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = setting.edit();
                                    editor.putString("name",name);
                                    editor.putString("email",email);
                                    editor.putString("facebook_id",id);
                                    editor.apply();
                                    Intent homeIntent=new Intent(getActivity(),MainActivity.class);
                                    startActivity(homeIntent);
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        }
                );
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error)
            {
                error.printStackTrace();
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean submitForm()
    {
        mobileNum=mobile.getText().toString().trim();
        passWord=password.getText().toString().trim();
        if(mobileNum.isEmpty() || passWord.isEmpty())
            return false;
        else
            return true;
    }

    private void checkValidation(String mobileNum, String passWord)
    {
        if(mobileNum.isEmpty())
        {
            mobile.setError("Please enter a phone number");
        }
        if(passWord.isEmpty())
        {
            password.setError("Please enter a password");
        }
    }

    private void checkLogin(String mobile, String password)
    {
        String urlLogin="http://rjtmobile.com/ansari/fos/fosapp/fos_login.php?&user_phone="+mobile+"&user_password="+password;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,urlLogin, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.d(TAG, response.toString());
                try
                {
                    JSONArray array = response.getJSONArray("msg");
                    String message=array.getString(0);
                    if(message.equals("success"))
                    {
//                        Toast.makeText(getBaseContext(),"Login success!",Toast.LENGTH_SHORT).show();
                        String mobile=array.getString(1);
                        // store data
                        SharedPreferences setting=getActivity().getSharedPreferences("Food@Door",MODE_PRIVATE);
                        SharedPreferences.Editor editor = setting.edit();
                        editor.putString("mobile",mobile);
                        editor.apply();
                        Intent homeIntent=new Intent(getActivity(),MainActivity.class);
                        startActivity(homeIntent);
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Invalid mobile or/and password",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        // Adding request to request queue
        AppController.getInstance().addRequestQueue(req);
    }

    @Override
    public void onPause()
    {
        if (mAdView != null)
        {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume()
    {
        if (mAdView != null)
        {
            mAdView.resume();
        }
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        if (mAdView != null)
        {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
