package com.adancruz.cedehaaapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListNotificationsAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context context;
    private ArrayList<Notificacion> listItems;

    public ListNotificationsAdapter(Context context, ArrayList<Notificacion> listItems) {
        this.context = context;
        this.listItems = listItems;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.item_notifications_list, null);
        final Notificacion notificacion = (Notificacion) getItem(position);

        ImageView imagen = (ImageView) view.findViewById(R.id.imagen_de_notificacion);
        TextView curso = (TextView) view.findViewById(R.id.texto_nombre_del_curso);
        TextView usuario = (TextView) view.findViewById(R.id.texto_tipo_y_nombre_de_usuario);
        TextView mensaje = (TextView) view.findViewById(R.id.texto_mensaje_de_notificacion);
        TextView fecha = (TextView) view.findViewById(R.id.texto_fecha_notificacion);

        String usuario_ = notificacion.getTipoUsuario() + ": " + notificacion.getNombreUsuario();

        imagen.setImageResource(notificacion.getImagen());
        curso.setText(notificacion.getNombreCurso());
        usuario.setText(usuario_);
        mensaje.setText(notificacion.getMensaje());
        fecha.setText(notificacion.getFecha());

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, YourCoursesDetailsActivity.class);
                intent.putExtra("objectData", notificacion);
                context.startActivity(intent);
            }
        });*/

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