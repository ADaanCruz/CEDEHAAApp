package com.adancruz.cedehaaapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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

    private SwipeRefreshLayout swipeRefresh;
    private TextView bienvenida, sinCursos;
    ListView cursos;
    Button nuevoCurso;
    ArrayList<Curso> arrayList = new ArrayList<>();
    Intent intent;
    String tipoDeUsuario = "";

    private ConnectivityManager con;
    private NetworkInfo network;
    private AlertDialog.Builder dialog;
    private boolean internet;
    private boolean selectionDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_st_home, container, false);

        swipeRefresh = view.findViewById(R.id.swipe_refresh_home);
        bienvenida = view.findViewById(R.id.texto_st_bienvenida);
        nuevoCurso = view.findViewById(R.id.boton_crear_curso);
        sinCursos = view.findViewById(R.id.texto_sin_cursos);
        cursos = view.findViewById(R.id.lista_st_cursos);

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

        verifyView(view);

        nuevoCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internet = verifyInternet(v.getContext());
                if (internet) {
                    intent = new Intent(view.getContext(), CreateCourseActivity.class);
                    intent.putExtra("editar",false);
                    intent.putExtra("tipoDeUsuario", tipoDeUsuario);
                    view.getContext().startActivity(intent);
                } else {
                    selectionDialog = false;
                    mostrarDialog(
                            v.getContext().getString(R.string.verify_internet_dialog_message),
                            v.getContext().getString(R.string.verify_internet_dialog_title),
                            "Entendido",
                            "Configuración",
                            view.getContext(),
                            "wifi"
                    );
                }
            }
        });

        swipeRefresh.setDistanceToTriggerSync(20);
        swipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                verifyView(view);
                swipeRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    private void cargarLista(final View vista) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, DireccionesURL.COURSE_REQUEST_URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("cursos");
                            arrayList = new ArrayList<>();
                            //cursos.removeAllViews();
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
                                swipeRefresh.setEnabled(false);
                            } else {
                                cursos.setAdapter(new ListCoursesAdapter(vista.getContext(), arrayList, tipoDeUsuario, false));
                                cursos.setVisibility(View.VISIBLE);
                                sinCursos.setVisibility(View.GONE);
                                swipeRefresh.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(vista.getContext(),
                                    "Error del servidor",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(vista.getContext());
        queue.add(request);
    }

    private void verifyView(View view) {
        sinCursos.setVisibility(View.GONE);
        internet = verifyInternet(view.getContext());
        if (!internet) {
            cursos.setVisibility(View.GONE);
            sinCursos.setVisibility(View.VISIBLE);
            sinCursos.setText(view.getContext().getString(R.string.no_internet));
        } else {
            sinCursos.setText(view.getContext().getString(R.string.sin_cursos));
            cargarLista(view);
        }
    }

    private boolean verifyInternet(Context context) {
        con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        network = con.getActiveNetworkInfo();
        return network != null && network.isConnected();
    }

    private void mostrarDialog(String mensaje, String titulo, String positive, String negative, final Context context, final String metodo){
        dialog = new AlertDialog.Builder(context);
        dialog.setMessage(mensaje)
                .setTitle(titulo);
        dialog.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectionDialog = true;

            }
        });

        if (negative != null) {
            dialog.setNegativeButton(negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectionDialog = false;
                    /*switch (metodo) {
                        case "wifi": context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            break;
                        default:
                            break;
                    }*/
                    if (metodo.equals("wifi")) {
                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }
            });
        }
        dialog.show();
    }
}