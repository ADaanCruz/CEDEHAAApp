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

import java.util.ArrayList;

public class TeHomeFragment extends Fragment {

    TextView bienvenida;
    ListView tusCursos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_te_home, container, false);

        bienvenida = (TextView) view.findViewById(R.id.texto_te_bienvenida);
        tusCursos = (ListView) view.findViewById(R.id.lista_te_tus_cursos);

        if (getArguments() != null) {
            String texto = "¡Bienvenido,\nprofesor " + getArguments().getString("nombre") + "!";
            bienvenida.setText(texto);
        }

        tusCursos.setAdapter(new ListCoursesAdapter(view.getContext(), GetArrayItems()));
        /*tusCursos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), YourCoursesDetailsActivity.class);
                intent.putExtra("objectData", GetArrayItems().get(position));
                startActivity(intent);
            }
        });*/

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
