package com.adancruz.cedehaaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class TeacherActivity extends AppCompatActivity {

    Fragment fragment = new TeHomeFragment();
    Bundle bundle = new Bundle();

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
                default:
                    selectedFragment = new TeHomeFragment();
                    break;
            }
            cargarBundle(selectedFragment);
            getSupportFragmentManager().beginTransaction().replace(R.id.te_container_activity,
                    selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        cargarBundle(fragment);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.te_navView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.te_container_activity,
                fragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(TeacherActivity.this, LoginActivity.class));
    }

    public void cargarBundle(Fragment selectedFragment) {
        String nombre = this.getIntent().getStringExtra("nombre");
        String apellidoPaterno = this.getIntent().getStringExtra("apellidoPaterno");
        String apellidoMaterno = this.getIntent().getStringExtra("apellidoMaterno");
        String correo = this.getIntent().getStringExtra("correo");

        bundle.putString("nombre", nombre);
        bundle.putString("apellidoPaterno", apellidoPaterno);
        bundle.putString("apellidoMaterno", apellidoMaterno);
        bundle.putString("correo", correo);
        selectedFragment.setArguments(bundle);
    }

}
