package com.adancruz.cedehaaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListSolicitudesAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context context;
    private ArrayList<Solicitud> listItems;
    private String tipoDeUsuario;

    public ListSolicitudesAdapter(Context context, ArrayList<Solicitud> listItems, String tipoDeUsuario) {
        this.context = context;
        this.listItems = listItems;
        this.tipoDeUsuario = tipoDeUsuario;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.item_solicitud_list, null);
        final Solicitud solicitud = (Solicitud) getItem(position);

        TextView titulo = (TextView) view.findViewById(R.id.texto_curso_solicita);
        TextView fecha = (TextView) view.findViewById(R.id.texto_fecha_curso_solicita);
        TextView nombre = (TextView) view.findViewById(R.id.texto_nombre_solicita);
        TextView apellidoPaterno = (TextView) view.findViewById(R.id.texto_apellido_pat_solicita);
        TextView correo = (TextView) view.findViewById(R.id.texto_correo_solicita);
        TextView telefono = (TextView) view.findViewById(R.id.texto_telefono_solicita);
        Button llamar = (Button) view.findViewById(R.id.boton_llamar_solicitud);
        Button aceptar = (Button) view.findViewById(R.id.boton_aceptar_solicitud);
        Button rechazar = (Button) view.findViewById(R.id.boton_rechazar_solicitud) ;

        titulo.setText(solicitud.getTitulo());
        fecha.setText(solicitud.getFechaInicio(true));
        nombre.setText(solicitud.getNombre());
        apellidoPaterno.setText(solicitud.getApellidoPaterno());
        correo.setText(solicitud.getCorreo());
        telefono.setText(solicitud.getTelefono());

        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(),
                        "Llamando...",
                        Toast.LENGTH_LONG).show();
            }
        });
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(),
                        "Aceptado",
                        Toast.LENGTH_LONG).show();
            }
        });
        rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(),
                        "Rechazado",
                        Toast.LENGTH_LONG).show();
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

}
