package com.adancruz.cedehaaapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListSolicitudesAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private ArrayList<Solicitud> listItems;
    private Response.Listener<String> stringListener;

    public ListSolicitudesAdapter(Context context, ArrayList<Solicitud> listItems) {
        this.listItems = listItems;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.item_solicitud_list, null);
        final Solicitud solicitud = (Solicitud) getItem(position);

        TextView titulo = view.findViewById(R.id.texto_curso_solicita);
        TextView fecha = view.findViewById(R.id.texto_fecha_curso_solicita);
        TextView nombre = view.findViewById(R.id.texto_nombre_solicita);
        TextView apellidoPaterno = view.findViewById(R.id.texto_apellido_pat_solicita);
        TextView correo = view.findViewById(R.id.texto_correo_solicita);
        TextView telefono = view.findViewById(R.id.texto_telefono_solicita);
        Button llamar = view.findViewById(R.id.boton_llamar_solicitud);
        Button aceptar = view.findViewById(R.id.boton_aceptar_solicitud);
        Button rechazar = view.findViewById(R.id.boton_rechazar_solicitud) ;

        final String cursoTitulo = solicitud.getTitulo();
        final String cursoFecha = solicitud.getFechaInicio(true);
        final String usuarioNombre = solicitud.getNombre();
        String usuarioApellidoPaterno = solicitud.getApellidoPaterno();
        final String usuarioCorreo = solicitud.getCorreo();
        String usuarioTelefono = solicitud.getTelefono();

        titulo.setText(cursoTitulo);
        fecha.setText(cursoFecha);
        nombre.setText(usuarioNombre);
        apellidoPaterno.setText(usuarioApellidoPaterno);
        correo.setText(usuarioCorreo);
        telefono.setText(usuarioTelefono);

        final Button[] botones = new Button[3];
        botones[0] = llamar;
        botones[1] = aceptar;
        botones[2] = rechazar;

        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonesEnable(botones, false);
                Toast.makeText(view.getContext(),
                        "Llamando...",
                        Toast.LENGTH_LONG).show();
                botonesEnable(botones, true);
            }
        });
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonesEnable(botones, false);
                stringListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(view.getContext(),
                                        "Solicitud aceptada",
                                        Toast.LENGTH_LONG).show();
                                botonesVisible(botones, false);
                            } else {
                                String error = jsonObject.getString("message");
                                String post = "post",
                                        cursoyusuario = "cursoyusuario",
                                        boton = "boton",
                                        totalylimite = "totalylimite",
                                        lleno = "lleno",
                                        update = "update";
                                if (error.equals(post)) {
                                    Toast.makeText(view.getContext(),
                                            "Error: Type POST", Toast.LENGTH_LONG).show();
                                } else if (error.equals(cursoyusuario)) {
                                    Toast.makeText(view.getContext(),
                                            "Hay un problema con el usuario", Toast.LENGTH_LONG).show();
                                } else if (error.equals(boton)) {
                                    Toast.makeText(view.getContext(),
                                            "Hay un problema con el botón", Toast.LENGTH_LONG).show();
                                } else if (error.equals(totalylimite)) {
                                    Toast.makeText(view.getContext(),
                                            "Hay un problema con el curso", Toast.LENGTH_LONG).show();
                                } else if (error.equals(lleno)) {
                                     Toast.makeText(view.getContext(),
                                             "El curso se acaba de llenar", Toast.LENGTH_LONG).show();
                                } else if (error.equals(update)) {
                                    Toast.makeText(view.getContext(),
                                        "Error: Type SQL - Update",
                                        Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(view.getContext(),
                                            "Algo salió mal", Toast.LENGTH_LONG).show();
                                }
                                botonesEnable(botones, true);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(view.getContext(),
                                    "ERROR: "+e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            botonesEnable(botones, true);
                        }
                    }
                };

                SolicitudesRequest solicitudesRequest = new SolicitudesRequest(
                        "aceptar",
                        cursoTitulo,
                        solicitud.getFechaInicio(false),
                        usuarioCorreo,
                        stringListener
                );
                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                queue.add(solicitudesRequest);
            }
        });
        rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonesEnable(botones, false);
                stringListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(view.getContext(),
                                        "Solicitud rechazada",
                                        Toast.LENGTH_LONG).show();
                                botonesVisible(botones, false);
                            } else {
                                String error = jsonObject.getString("message");
                                String post = "post",
                                        cursoyusuario = "cursoyusuario",
                                        boton = "boton",
                                        delete = "delete";
                                if (error.equals(post)) {
                                    Toast.makeText(view.getContext(),
                                            "Error: Type POST", Toast.LENGTH_LONG).show();
                                } else if (error.equals(cursoyusuario)) {
                                    Toast.makeText(view.getContext(),
                                            "Hay un problema con el usuario", Toast.LENGTH_LONG).show();
                                } else if (error.equals(boton)) {
                                    Toast.makeText(view.getContext(),
                                            "Hay un problema con el botón", Toast.LENGTH_LONG).show();
                                } else if (error.equals(delete)) {
                                    Toast.makeText(view.getContext(),
                                            "Error: Type SQL - delete", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(view.getContext(),
                                            "Algo salió mal", Toast.LENGTH_LONG).show();
                                }
                                botonesEnable(botones, true);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(view.getContext(),
                                    "ERROR: "+e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            botonesEnable(botones, true);
                        }

                    }
                };

                SolicitudesRequest solicitudesRequest = new SolicitudesRequest(
                        "rechazar",
                        cursoTitulo,
                        solicitud.getFechaInicio(false),
                        usuarioCorreo,
                        stringListener
                );
                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                queue.add(solicitudesRequest);
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private void botonesEnable(Button[] botones, boolean enable) {
        for (Button boton : botones) {
            boton.setEnabled(enable);
        }
    }

    private void botonesVisible (Button[] botones, boolean visible) {
        if (visible) {
            for (Button boton : botones) {
                boton.setVisibility(View.VISIBLE);
            }
        } else {
            for (Button boton : botones) {
                boton.setVisibility(View.GONE);
            }
        }
    }

}
