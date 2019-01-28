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

public class StNotificationsFragment extends Fragment {

    ListView notificaciones;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_st_notifications, container, false);

        notificaciones = (ListView) view.findViewById(R.id.lista_notificaciones);
        notificaciones.setAdapter(new ListNotificationsAdapter(view.getContext(), GetArrayItems()));

        return view;
    }

    private ArrayList<Notificacion> GetArrayItems() {
        ArrayList<Notificacion> listItems = new ArrayList<>();

        listItems.add(new Notificacion(R.drawable.placeholder, "Curso 1",
                "Grupo 1", "Profesor", "Usuario 1",
                "Mensaje 1", "dd/mm/yy"));
        listItems.add(new Notificacion(R.drawable.placeholder, "Curso 2",
                "Grupo 2", "Profesor", "Usuario 2",
                "Mensaje 2", "dd/mm/yy"));
        listItems.add(new Notificacion(R.drawable.placeholder, "Curso 3",
                "Grupo 3", "Profesor", "Usuario 3",
                "Mensaje 3", "dd/mm/yy"));
        listItems.add(new Notificacion(R.drawable.placeholder, "Curso 4",
                "Grupo 4", "Profesor", "Usuario 4",
                "Mensaje 4", "dd/mm/yy"));
        listItems.add(new Notificacion(R.drawable.placeholder, "Curso 5",
                "Grupo 5", "Profesor", "Usuario 5",
                "Mensaje 5", "dd/mm/yy"));
        listItems.add(new Notificacion(R.drawable.placeholder, "Curso 6",
                "Grupo 6", "Profesor", "Usuario 6",
                "Mensaje 6", "dd/mm/yy"));
        listItems.add(new Notificacion(R.drawable.placeholder, "Curso 7",
                "Grupo 7", "Profesor", "Usuario 7",
                "Mensaje 7", "dd/mm/yy"));

        return listItems;
    }
}
