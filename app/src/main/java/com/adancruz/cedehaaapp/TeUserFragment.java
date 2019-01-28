package com.adancruz.cedehaaapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TeUserFragment extends Fragment {

    TextView nombre;
    TextView apellidos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_te_user, container, false);

        nombre = (TextView) view.findViewById(R.id.texto_te_nombre);
        apellidos = (TextView) view.findViewById(R.id.texto_te_apellidos);

        if (getArguments() != null) {
            String nombre_ = getArguments().getString("nombre");
            String apellidos_ = getArguments().getString("apellidoPaterno") + " " +
                    getArguments().getString("apellidoMaterno");

            nombre.setText(nombre_);
            apellidos.setText(apellidos_);
        }

        return view;
    }
}
