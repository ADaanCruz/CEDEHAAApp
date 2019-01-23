package com.adancruz.cedehaaapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class StHomeFragment extends Fragment {

    TextView bienvenida;
    ListView tusCursos;
    String[][] datos = {
            {"Curso 1","Descripción breve 1"},
            {"Curso 2","Descripción breve 2"},
            {"Curso 3","Descripción breve 3"},
            {"Curso 4","Descripción breve 4"},
            {"Curso 5","Descripción breve 5"},
            {"Curso 6","Descripción breve 6"}
    };
    int[] datosImg = {R.drawable.placeholder, R.drawable.placeholder, R.drawable.placeholder,
            R.drawable.placeholder, R.drawable.placeholder, R.drawable.placeholder};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_st_home, container, false);

        bienvenida = (TextView) view.findViewById(R.id.texto_bienvenida);
        tusCursos = (ListView) view.findViewById(R.id.tus_cursos);

        if (getArguments() != null) {
            String texto = "¡Bienvenido, alumno " + getArguments().getString("nombre") + "!";
            bienvenida.setText(texto);
        }

        tusCursos.setAdapter(new ListYourCoursesAdapter(view.getContext(), datos, datosImg));

        return view;
    }
}
