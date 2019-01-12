package com.adancruz.cedehaaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class TeacherActivity extends AppCompatActivity {

    private Intent intent;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.te_navigation_home:
                    selectedFragment = new TeHomeFragment();
                    break;
                case R.id.te_navigation_courses:
                    selectedFragment = new TeCoursesFragment();
                    break;
                case R.id.te_navigation_notifications:
                    selectedFragment = new TeNotificationsFragment();
                    break;
                case R.id.te_navigation_user:
                    selectedFragment = new TeUserFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.te_container_activity,
                    selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.te_navView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.te_container_activity,
                new TeHomeFragment()).commit();
    }

}
