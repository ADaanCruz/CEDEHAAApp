package com.adancruz.cedehaaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class StudentActivity extends AppCompatActivity {

    Fragment fragment = new StHomeFragment();
    Bundle bundle = new Bundle();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment;
            switch (item.getItemId()) {
                case R.id.st_navigation_home:
                    selectedFragment = fragment;
                    break;
                case R.id.st_navigation_courses:
                    selectedFragment = new StCoursesFragment();
                    break;
                case R.id.st_navigation_notifications:
                    selectedFragment = new StNotificationsFragment();
                    break;
                case R.id.st_navigation_user:
                    selectedFragment = new StUserFragment();
                    break;
                default:
                    selectedFragment = new StHomeFragment();
                    break;
            }
            cargarBundle(selectedFragment);
            getSupportFragmentManager().beginTransaction().replace(R.id.st_container_activity,
                    selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        cargarBundle(fragment);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.st_navView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.st_container_activity,
                fragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(StudentActivity.this, LoginActivity.class));
    }

    public void cargarBundle(Fragment selectedFragment) {
        String nombre = this.getIntent().getStringExtra("nombre");
        String apellidoPaterno = this.getIntent().getStringExtra("apellidoPaterno");
        String apellidoMaterno = this.getIntent().getStringExtra("apellidoMaterno");
        String correo = this.getIntent().getStringExtra("correo");
        String telefono = this.getIntent().getStringExtra("telefono");

        bundle.putString("nombre", nombre);
        bundle.putString("apellidoPaterno", apellidoPaterno);
        bundle.putString("apellidoMaterno", apellidoMaterno);
        bundle.putString("correo", correo);
        bundle.putString("telefono", telefono);
        selectedFragment.setArguments(bundle);
    }
}