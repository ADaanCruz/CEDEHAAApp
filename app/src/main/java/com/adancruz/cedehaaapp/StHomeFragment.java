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

public class StHomeFragment extends Fragment {

    private static final String COURSE_REQUEST_URL = "http://projects-as-a-developer.online/all-courses.php";

    TextView bienvenida, sinCursos;
    ListView cursos;
    Button nuevoCurso;
    ArrayList<Curso> arrayList = new ArrayList<>();
    Intent intent;
    String tipoDeUsuario = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_st_home, container, false);

        bienvenida = view.findViewById(R.id.texto_st_bienvenida);
        nuevoCurso = view.findViewById(R.id.boton_crear_curso);
        sinCursos = view.findViewById(R.id.texto_sin_cursos);
        cursos = view.findViewById(R.id.lista_st_cursos);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, COURSE_REQUEST_URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("cursos");
                            arrayList = new ArrayList<>();
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
                                cursos.setVisibility(View.GONE);
                                sinCursos.setVisibility(View.VISIBLE);
                            } else {
                                cursos.setAdapter(new ListCoursesAdapter(view.getContext(), arrayList, tipoDeUsuario, false));
                                cursos.setVisibility(View.VISIBLE);
                                sinCursos.setVisibility(View.GONE);
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

        if (getArguments() != null) {
            String texto = "¡Bienvenido, " + getArguments().getString("nombre") + "!";
            bienvenida.setText(texto);
            tipoDeUsuario = getArguments().getString("tipoDeUsuario");
            if(tipoDeUsuario != null && tipoDeUsuario.equals("administrador")) {
                nuevoCurso.setVisibility(View.VISIBLE);
            } else{
                nuevoCurso.setVisibility(View.GONE);
            }
        }

        nuevoCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(view.getContext(), CreateCourseActivity.class);
                intent.putExtra("editar",false);
                intent.putExtra("tipoDeUsuario", tipoDeUsuario);
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }
}