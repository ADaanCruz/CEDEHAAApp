package com.adancruz.cedehaaapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class StCoursesFragment extends Fragment {

    ListView cursos;
    ArrayList<Curso> arrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_st_courses, container, false);

        cursos = (ListView) view.findViewById(R.id.lista_cursos);
        cursos.setAdapter(new ListCoursesAdapter(view.getContext(), GetArrayItems(), "administrador"));

        /*final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replace("][",",");
                if (response.length() > 0) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        Log.i("sizejson",""+ jsonArray.length());
                        arrayList = CargarLista(jsonArray, view);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(view.getContext(),
                                "ERROR: "+e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        };

        CourseRequest courseRequest = new CourseRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        queue.add(courseRequest);

        Toast.makeText(view.getContext(),
                arrayList.size()+"",
                Toast.LENGTH_LONG).show();

        if (arrayList.size() == 0) {
            cursos.setVisibility(View.GONE);
        } else {
            cursos.setVisibility(View.VISIBLE);
            cursos.setAdapter(new ListYourCoursesAdapter(view.getContext(), arrayList));
            /*cursos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(view.getContext(), YourCoursesDetailsActivity.class);
                    intent.putExtra("objectData", GetArrayItems().get(position));
                    startActivity(intent);
                }
            });
        }*/

        return view;
    }

    public ArrayList<Curso> CargarLista(JSONArray jsonArray, View view) {
        ArrayList<Curso> listItems = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i += 8) {
            try {
                listItems.add(new Curso(
                        jsonArray.getString(i+1),
                        R.drawable.placeholder,
                        jsonArray.getString(i+3),
                        jsonArray.getString(i+4),
                        jsonArray.getString(i+5),
                        0,
                        20));
            } catch (JSONException e) {
                Toast.makeText(view.getContext(),
                        "ERROR: "+e.getMessage(),
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        return listItems;
    }

    private ArrayList<Curso> GetArrayItems() {
        ArrayList<Curso> listItems = new ArrayList<>();

        listItems.add(new Curso("Titulo 99", R.drawable.placeholder, "Desc Breve 99",
                "Desc General 99", "2019-02-20", 0, 20));
        /*listItems.add(new Curso(R.drawable.placeholder, "Titulo 2",
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
                "Descripción 7", "Desc General 7"));*/

        return listItems;
    }
}
