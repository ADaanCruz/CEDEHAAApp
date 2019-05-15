package com.adancruz.cedehaaapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class StudentActivity extends AppCompatActivity {

    Fragment fragment = new StHomeFragment();
    BottomNavigationView navigation;
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
        navigation = findViewById(R.id.st_navView);
        MenuItem opcion2 = navigation.getMenu().getItem(1);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);
        String tipoDeUsuario = prefs.getString("tipoDeUsuario", "estudiante");
        if (tipoDeUsuario != null && tipoDeUsuario.equals("administrador")) {
            opcion2.setTitle("Solicitudes");
        } else {
            opcion2.setTitle("Cursos");
        }

        cargarBundle(fragment);

        BottomNavigationView navigation = findViewById(R.id.st_navView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.st_container_activity,
                fragment).commit();
    }

    public static final String MY_PREFS_FILENAME = "com.adancruz.cedehaaappp.User";

    public void cargarBundle(Fragment selectedFragment) {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);

        String nombre = prefs.getString("nombre", null);
        String apellidoPaterno = prefs.getString("apellidoPaterno", null);
        String apellidoMaterno = prefs.getString("apellidoMaterno", null);
        String correo = prefs.getString("correo", null);
        String contrasena = prefs.getString("contrasena", null);
        String telefono = prefs.getString("numero", null);
        String tipoDeUsuario = prefs.getString("tipoDeUsuario", null);

        bundle.putString("nombre", nombre);
        bundle.putString("apellidoPaterno", apellidoPaterno);
        bundle.putString("apellidoMaterno", apellidoMaterno);
        bundle.putString("correo", correo);
        bundle.putString("contrasena", contrasena);
        bundle.putString("numero", telefono);
        bundle.putString("tipoDeUsuario", tipoDeUsuario);
        selectedFragment.setArguments(bundle);
    }
}