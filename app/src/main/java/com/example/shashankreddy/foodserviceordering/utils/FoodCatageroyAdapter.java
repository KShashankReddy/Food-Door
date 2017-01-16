package com.example.shashankreddy.foodserviceordering.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.shashankreddy.foodserviceordering.fragments.NonVegFragment;
import com.example.shashankreddy.foodserviceordering.fragments.VegFragment;


public class FoodCatageroyAdapter extends FragmentStatePagerAdapter {
    int tabCount;
    public FoodCatageroyAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position){
            case 0:
                fragment = new VegFragment();
                break;
            case 1:
                fragment = new NonVegFragment();
                break;
            default:
                fragment = null;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle;
        switch (position){
            case 0:
                pageTitle = "VEG FOOD";
                break;
            case 1:
                pageTitle = "NON-VEG FOOD";
                break;
            default:
                pageTitle=null;
                break;
        }
        return pageTitle;
    }
}
