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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StNotificationsFragment extends Fragment {

    private static final String MY_COURSES_REQUEST_URL = "https://cedehaa-app.000webhostapp.com/my-courses.php";

    ArrayList<Curso> arrayList = new ArrayList<Curso>();

    ListView misCursos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_st_notifications, container, false);

        misCursos = (ListView) view.findViewById(R.id.lista_notificaciones);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MY_COURSES_REQUEST_URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("cursos");
                            arrayList = new ArrayList<Curso>();
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                Curso curso = new Curso(
                                        jsonObject.optString("titulo"),
                                        Integer.parseInt(jsonObject.optString("numImagen")),
                                        jsonObject.optString("descBreve"),
                                        jsonObject.optString("descGeneral"),
                                        jsonObject.optString("fechaInicio"),
                                        Integer.parseInt(jsonObject.optString("totalEstudiantes")),
                                        Integer.parseInt(jsonObject.optString("limiteEstudiantes")),
                                        jsonObject.optString("estado")
                                );
                                arrayList.add(curso);
                            }
                            if (arrayList.size() == 0) {

                            } else {
                                misCursos.setAdapter(new ListCoursesAdapter(view.getContext(), arrayList, "estudiante", true));
                                misCursos.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(view.getContext(),
                                    "Error: JSONException",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        queue.add(request);

        return view;
    }
}