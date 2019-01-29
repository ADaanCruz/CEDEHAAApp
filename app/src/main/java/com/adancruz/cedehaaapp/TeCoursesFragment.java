package com.adancruz.cedehaaapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class TeCoursesFragment extends Fragment {

    ListView cursos;
    ArrayList<Curso> arrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_te_courses, container, false);

        cursos = (ListView) view.findViewById(R.id.lista_te_cursos);
        cursos.setAdapter(new ListCoursesAdapter(view.getContext(), GetArrayItems()));

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
