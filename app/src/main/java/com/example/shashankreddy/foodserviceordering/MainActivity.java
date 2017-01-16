package com.example.shashankreddy.foodserviceordering;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.shashankreddy.foodserviceordering.fragments.PaymentFragment;
import com.example.shashankreddy.foodserviceordering.fragments.HomeFragment;
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
                        break;
                    case R.id.bottom_personalInfo :
                        break;
                    case R.id.bottom_checkout :
                        fragment = new PaymentFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.fragment_replaceable,fragment).commit();
                return true;
            }
        });
    }

}
