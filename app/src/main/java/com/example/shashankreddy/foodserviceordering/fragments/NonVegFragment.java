package com.example.shashankreddy.foodserviceordering.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shashankreddy.foodserviceordering.R;

public class NonVegFragment extends Fragment {

    public NonVegFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.food_catagery_layout,container,false);
        return retView;
    }
}
