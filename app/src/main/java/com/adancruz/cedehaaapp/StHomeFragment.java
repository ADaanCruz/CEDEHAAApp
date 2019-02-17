package com.adancruz.cedehaaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class StHomeFragment extends Fragment {

    TextView bienvenida;
    ListView cursos;
    Button nuevoCurso;
    Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_st_home, container, false);

        bienvenida = (TextView) view.findViewById(R.id.texto_st_bienvenida);
        cursos = (ListView) view.findViewById(R.id.lista_st_cursos);
        nuevoCurso = (Button) view.findViewById(R.id.boton_crear_curso);

        if (getArguments() != null) {
            String texto = "¡Bienvenido, " + getArguments().getString("nombre") + "!";
            bienvenida.setText(texto);
            if(getArguments().getString("tipoDeUsuario").equals("admin")) {
                nuevoCurso.setVisibility(View.VISIBLE);
            } else{
                nuevoCurso.setVisibility(View.GONE);
            }
        }

        cursos.setAdapter(new ListCoursesAdapter(view.getContext(), GetArrayItems()));
        nuevoCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(view.getContext(), CreateCourseActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }

    private ArrayList<Curso> GetArrayItems() {
        ArrayList<Curso> listItems = new ArrayList<>();

        listItems.add(new Curso(R.drawable.placeholder, "Titulo 1",
                "Descripción 1", "Desc General 1"));
        listItems.add(new Curso(R.drawable.placeholder, "Titulo 2",
                "Descripción 2", "Desc General 2"));
        listItems.add(new Curso(R.drawable.placeholder, "Titulo 3",
                "Descripción 3", "Desc General 3"));
        listItems.add(new Curso(R.drawable.placeholder, "Titulo 4",
                "Descripción 4", "Desc General 4"));
        listItems.add(new Curso(R.drawable.placeholder, "Titulo 5",
                "Descripción 5", "Desc General 5"));
        listItems.add(new Curso(R.drawable.placeholder, "Titulo 6",
                "Descripción 6", "Desc General 6"));
        listItems.add(new Curso(R.drawable.placeholder, "Titulo 7",
                "Descripción 7", "Desc General 7"));

        return listItems;
    }
}
