package com.example.shashankreddy.foodserviceordering.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shashankreddy.foodserviceordering.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

/**
 * Created by Ricky on 2017/1/16.
 */

public class PaymentFragment extends Fragment {
    // private static final String TAG = "paymentdemoblog";
    /**
     * - Set to PaymentActivity.ENVIRONMENT_PRODUCTION to move real money.
     *
     * - Set to PaymentActivity.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    // private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AaiIOv_lbjNwuLeAooNUAGh8mR_ir228bVDqlvjBmEdPruoQnVWePjchcKBVEsESiq-H-BToZEtVyvcL";

    private static final int REQUEST_CODE_PAYMENT = 1;
//    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

    private static PayPalConfiguration config = new PayPalConfiguration().environment(CONFIG_ENVIRONMENT).clientId(CONFIG_CLIENT_ID);
    // The following are only used in PayPalFuturePaymentActivity.
//            .merchantName("Hipster Store")
//            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
//            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    PayPalPayment thingToBuy;
    String name;
    int final_price;
    private static final String TAG = "Checkout";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // start service
        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_payment, container, false);

        // take order
        v.findViewById(R.id.order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                thingToBuy = new PayPalPayment(new BigDecimal(final_price), "USD",name, PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(getActivity(),PaymentActivity.class);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
            }
        });
        // cancel order
        v.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Order is canceled", Toast.LENGTH_SHORT).show();
                HomeFragment fragment = new HomeFragment();
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.fragment_replaceable,fragment).commit();
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE_PAYMENT)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null)
                {
                    try
                    {
                        System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject().toString(4));
                        // register order
                        registerOrder();
                        Toast.makeText(getActivity(), "Order placed",Toast.LENGTH_SHORT).show();
                        // back to Home
                        HomeFragment fragment = new HomeFragment();
                        FragmentManager fm=getFragmentManager();
                        FragmentTransaction ft=fm.beginTransaction();
                        ft.replace(R.id.fragment_replaceable,fragment).commit();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED)
            {
                System.out.println("The order is canceled.");
            }
            else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            {
                System.out.println("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void registerOrder()
    {
        // use OrderRequest to place order
    }

    @Override
    public void onDestroy()
    {
        // Stop service when done
        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }
}
