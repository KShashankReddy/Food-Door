package com.example.shashankreddy.foodserviceordering.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shashankreddy.foodserviceordering.R;
import com.example.shashankreddy.foodserviceordering.appController.AppController;
import com.example.shashankreddy.foodserviceordering.models.FoodItemList;
import com.example.shashankreddy.foodserviceordering.utils.FoodCatageroyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_home, container, false);
        mTabLayout = (TabLayout) returnView.findViewById(R.id.food_catatgeory_tab_layout);
        mViewPager = (ViewPager) returnView.findViewById(R.id.food_catageroy_view_pager);
        mToolbar = (Toolbar) returnView.findViewById(R.id.hometoolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);

        manageToolBar();
        mTabLayout.addTab(mTabLayout.newTab().setText("Veg Food"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Non Veg Food"));
        FoodCatageroyAdapter foodCatageroyAdapter = new FoodCatageroyAdapter(getChildFragmentManager(),mTabLayout.getTabCount());
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mViewPager.setAdapter(foodCatageroyAdapter);
        getFoodList();
        return returnView;
    }

    private void manageToolBar() {
        TextView toAddress,fromAddress;
        View toAddressLayout,fromAddressLayout;

        toAddress = (TextView) mToolbar.findViewById(R.id.toAddress);
        fromAddress = (TextView) mToolbar.findViewById(R.id.fromAddress);
        toAddressLayout = mToolbar.findViewById(R.id.toAddressLayout);
        fromAddressLayout =mToolbar.findViewById(R.id.fromAddressLayout);
        toAddress.setText(AppController.getInstance().getmUserAddress().getAddress1());
        fromAddress.setText("Delhi");
        toAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(HomeFragment.class.getSimpleName(), "toAddress is clicked");
            }
        });
        fromAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(HomeFragment.class.getSimpleName(), "fromAddress is clicked");
            }
        });
    }

    private void getFoodList(){
        String foodListURL="http://rjtmobile.com/ansari/fos/fosapp/fos_food_loc.php?city=delhi";
        final ArrayList<FoodItemList> foodItemLists = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, foodListURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray foodList = response.getJSONArray("Food");
                    for(int i=0;i<foodList.length();i++){
                        JSONObject foodItem = foodList.getJSONObject(i);
                        String foodId = foodItem.getString("FoodId");
                        String foodName = foodItem.getString("FoodName");
                        String foodRecepiee = foodItem.getString("FoodRecepiee");
                        String foodPrice = foodItem.getString("FoodPrice");
                        String foodCat = foodItem.getString("FoodCategory");
                        String foodImage = foodItem.getString("FoodThumb");
                        FoodItemList foodItemList = new FoodItemList(foodId,foodName,foodRecepiee,foodPrice,foodCat,foodImage);
                        foodItemLists.add(foodItemList);
                    }

                    AppController.getInstance().getmFoodItemList().setmFoodItemLists(foodItemLists);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("city","delhi");
                Log.d(HomeFragment.class.getSimpleName(), params.toString());
                return params;
            }
        };
        Log.d(HomeFragment.class.getSimpleName(),jsonObjectRequest.getUrl());
        AppController.getInstance().addRequestQueue(jsonObjectRequest);
    }

}
