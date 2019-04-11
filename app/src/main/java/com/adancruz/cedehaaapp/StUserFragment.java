package com.adancruz.cedehaaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class StUserFragment extends Fragment {

    public static final String MY_PREFS_FILENAME = "com.adancruz.cedehaaappp.User";

    TextView nombre;
    TextView apellidos;
    TextView cerrar_sesion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_st_user, container, false);

        nombre = view.findViewById(R.id.texto_nombre);
        apellidos = view.findViewById(R.id.texto_apellidos);
        cerrar_sesion = view.findViewById(R.id.conf_cerrar_sesion);

        if (getArguments() != null) {
            String nombre_ = getArguments().getString("nombre");
            nombre.setText(nombre_);
            String apellidos_ = getArguments().getString("apellidoPaterno") + " " +
                  getArguments().getString("apellidoMaterno");
            apellidos.setText(apellidos_);
        }

        cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                cerrar_sesion.setEnabled(false);

                SharedPreferences.Editor editor = Objects.requireNonNull(getActivity()).getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE).edit();
                editor.putBoolean("sesion", false);
                editor.apply();

                Objects.requireNonNull(getActivity()).finish();
                startActivity(new Intent(view.getContext(), LoginActivity.class));
            }
        });

        return view;
    }
}