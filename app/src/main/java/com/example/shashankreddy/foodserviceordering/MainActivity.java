package com.example.shashankreddy.foodserviceordering;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.shashankreddy.foodserviceordering.fragments.CheckOutFragment;
import com.example.shashankreddy.foodserviceordering.fragments.HomeFragment;
import com.example.shashankreddy.foodserviceordering.fragments.OrdersFragment;
import com.example.shashankreddy.foodserviceordering.fragments.PersonalInfoFragment;
import com.example.shashankreddy.foodserviceordering.utils.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bootomNavigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.bottom_home :
                        fragment = new HomeFragment();
                        break;
                    case R.id.bottom_orders :
                        fragment = new OrdersFragment();
                        break;
                    case R.id.bottom_personalInfo :
                        fragment = new PersonalInfoFragment();
                        break;
                    case R.id.bottom_checkout :
                        fragment = new CheckOutFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.fragment_replaceable,fragment).commit();
                return true;
            }
        });
    }

}
