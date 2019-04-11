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

    private static String MY_COURSES_REQUEST_URL = "http://projects-as-a-developer.online/my-courses.php?correo=ninguno";
    private static String COURSE_SOLICIT_REQUEST_URL = "http://projects-as-a-developer.online/all-solicitudes.php";

    ArrayList<Curso> arrayListCur;
    ArrayList<Solicitud> arrayListSol;

    ListView lista;
    TextView sinLista, notificacion;

    String tipoDeUsuario = "estudiante";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_st_notifications, container, false);

        notificacion = view.findViewById(R.id.texto_notificaciones);
        lista = view.findViewById(R.id.lista_cursos_o_solicitudes);
        sinLista = view.findViewById(R.id.texto_sin_lista);

        if (getArguments() != null) {
            String correo = getArguments().getString("correo");
            tipoDeUsuario = getArguments().getString("tipoDeUsuario");
            MY_COURSES_REQUEST_URL = "http://projects-as-a-developer.online/my-courses.php?correo=" + correo;
        }

        JsonObjectRequest request;
        assert tipoDeUsuario != null;
        switch (tipoDeUsuario) {
            case "administrador": {
                sinLista.setText(getString(R.string.sin_solicitudes_cursos));
                notificacion.setText(getString(R.string.solicitudes));
                request = new JsonObjectRequest(Request.Method.GET, COURSE_SOLICIT_REQUEST_URL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("solicitudes");
                                    arrayListSol = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        Solicitud solicitud = new Solicitud(
                                                jsonObject.optString("titulo"),
                                                jsonObject.optString("fechaInicio"),
                                                jsonObject.optString("nombre"),
                                                jsonObject.optString("apellidoPaterno"),
                                                jsonObject.optString("correo"),
                                                jsonObject.optString("telefono")
                                        );

                                        arrayListSol.add(solicitud);
                                    }
                                    lista.setAdapter(new ListSolicitudesAdapter(view.getContext(), arrayListSol));
                                    lista.setVisibility(View.VISIBLE);
                                    sinLista.setVisibility(View.GONE);
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
                        lista.setVisibility(View.GONE);
                        sinLista.setVisibility(View.VISIBLE);
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                queue.add(request);

                break;
            }
            case "estudiante": {
                sinLista.setText(getString(R.string.sin_curso_inscrito));
                notificacion.setText(getString(R.string.tus_cursos));
                request = new JsonObjectRequest(Request.Method.GET, MY_COURSES_REQUEST_URL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("cursos");
                                    arrayListCur = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
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
                                        arrayListCur.add(curso);
                                    }
                                    lista.setAdapter(new ListCoursesAdapter(view.getContext(), arrayListCur, "estudiante", true));
                                    lista.setVisibility(View.VISIBLE);
                                    sinLista.setVisibility(View.GONE);
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
                        lista.setVisibility(View.GONE);
                        sinLista.setVisibility(View.VISIBLE);
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                queue.add(request);
                break;
            }
            default:
                notificacion.setText(getString(R.string.notificaciones));
                sinLista.setText(getString(R.string.error_tipo_de_usuario));

                lista.setVisibility(View.GONE);
                sinLista.setVisibility(View.VISIBLE);
                break;
        }

        return view;
    }
}