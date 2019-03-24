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

    private static String MY_COURSES_REQUEST_URL = "https://cedehaa-app.000webhostapp.com/my-courses.php?correo=ninguno";
    private static String SOLICITUDES_REQUEST_URL = "https://cedehaa-app.000webhostapp.com/solicitudes.php";

    ArrayList<Curso> arrayListCur;
    ArrayList<Solicitud> arrayListSol;

    ListView lista;
    TextView sinLista, notificacion;

    String tipoDeUsuario = "estudiante";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_st_notifications, container, false);

        notificacion = (TextView) view.findViewById(R.id.texto_notificaciones);
        lista = (ListView) view.findViewById(R.id.lista_cursos_o_solicitudes);
        sinLista = (TextView) view.findViewById(R.id.texto_sin_lista);

        if (getArguments() != null) {
            String correo = getArguments().getString("correo");
            tipoDeUsuario = getArguments().getString("tipoDeUsuario");
            MY_COURSES_REQUEST_URL = "https://cedehaa-app.000webhostapp.com/my-courses.php?correo=" + correo;
        }

        JsonObjectRequest request;
        if (tipoDeUsuario.equals("administrador")) {
            notificacion.setText(getString(R.string.solicitudes));
            request = new JsonObjectRequest(Request.Method.GET, SOLICITUDES_REQUEST_URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("solicitudes");
                                arrayListSol = new ArrayList<Solicitud>();
                                for (int i=0; i<jsonArray.length(); i++) {
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
                                if (arrayListSol.size() == 0) {
                                    sinLista.setText(getString(R.string.sin_solicitudes_curso));

                                    lista.setVisibility(View.GONE);
                                    sinLista.setVisibility(View.VISIBLE);
                                } else {
                                    lista.setAdapter(new ListSolicitudesAdapter(view.getContext(), arrayListSol, "administrador"));
                                    lista.setVisibility(View.VISIBLE);
                                    sinLista.setVisibility(View.GONE);
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
                    Toast.makeText(view.getContext(),
                            "Error: VollleyError",
                            Toast.LENGTH_LONG).show();
                }
            });

            RequestQueue queue = Volley.newRequestQueue(view.getContext());
            queue.add(request);

        } else if (tipoDeUsuario.equals("estudiante")) {
            notificacion.setText(getString(R.string.tus_cursos));
            request = new JsonObjectRequest(Request.Method.GET, MY_COURSES_REQUEST_URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("cursos");
                                arrayListCur = new ArrayList<Curso>();
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
                                    arrayListCur.add(curso);
                                }
                                if (arrayListCur.size() == 0) {
                                    sinLista.setText(getString(R.string.sin_curso_inscrito));

                                    lista.setVisibility(View.GONE);
                                    sinLista.setVisibility(View.VISIBLE);
                                } else {
                                    lista.setAdapter(new ListCoursesAdapter(view.getContext(), arrayListCur, "estudiante"));
                                    lista.setVisibility(View.VISIBLE);
                                    sinLista.setVisibility(View.GONE);
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
                    Toast.makeText(view.getContext(),
                            "Error: VollleyError",
                            Toast.LENGTH_LONG).show();
                }
            });

            RequestQueue queue = Volley.newRequestQueue(view.getContext());
            queue.add(request);
        } else {
            notificacion.setText(getString(R.string.notificaciones));
            sinLista.setText(getString(R.string.error_tipo_de_usuario));

            lista.setVisibility(View.GONE);
            sinLista.setVisibility(View.VISIBLE);
        }

        return view;
    }
}