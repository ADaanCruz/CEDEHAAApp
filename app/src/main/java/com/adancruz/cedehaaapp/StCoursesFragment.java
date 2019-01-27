package com.adancruz.cedehaaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StCoursesFragment extends Fragment {

    ListView cursos;
    ArrayList<YourCourse> arrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_st_courses, container, false);

        cursos = (ListView) view.findViewById(R.id.lista_cursos);
        cursos.setAdapter(new ListYourCoursesAdapter(view.getContext(), GetArrayItems()));

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

    public ArrayList<YourCourse> CargarLista(JSONArray jsonArray, View view) {
        ArrayList<YourCourse> listItems = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i += 8) {
            try {
                listItems.add(new YourCourse(R.drawable.placeholder, jsonArray.getString(i+2),
                        jsonArray.getString(i+3), jsonArray.getString(i+4)));
            } catch (JSONException e) {
                Toast.makeText(view.getContext(),
                        "ERROR: "+e.getMessage(),
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        return listItems;
    }

    private ArrayList<YourCourse> GetArrayItems() {
        ArrayList<YourCourse> listItems = new ArrayList<>();

        listItems.add(new YourCourse(R.drawable.placeholder, "Titulo 1",
                "Descripción 1", "Desc General 1"));
        listItems.add(new YourCourse(R.drawable.placeholder, "Titulo 2",
                "Descripción 2", "Desc General 2"));
        listItems.add(new YourCourse(R.drawable.placeholder, "Titulo 3",
                "Descripción 3", "Desc General 3"));
        listItems.add(new YourCourse(R.drawable.placeholder, "Titulo 4",
                "Descripción 4", "Desc General 4"));
        listItems.add(new YourCourse(R.drawable.placeholder, "Titulo 5",
                "Descripción 5", "Desc General 5"));
        listItems.add(new YourCourse(R.drawable.placeholder, "Titulo 6",
                "Descripción 6", "Desc General 6"));
        listItems.add(new YourCourse(R.drawable.placeholder, "Titulo 7",
                "Descripción 7", "Desc General 7"));

        return listItems;
    }
}
