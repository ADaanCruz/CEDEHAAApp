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

public class ListCoursesAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context context;
    private ArrayList<Curso> listItems;
    private String tipoDeUsuario;
    private boolean notificacones;

    public ListCoursesAdapter(Context context, ArrayList<Curso> listItems,
                              String tipoDeUsuario, boolean notificaciones) {
        this.context = context;
        this.listItems = listItems;
        this.tipoDeUsuario = tipoDeUsuario;
        this.notificacones = notificaciones;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.item_course_list, null);
        final Curso curso = (Curso) getItem(position);

        TextView titulo = (TextView) view.findViewById(R.id.texto_titulo_del_curso);
        ImageView imagen = (ImageView) view.findViewById(R.id.imagen_item_del_curso);
        TextView descripcionBreve = (TextView) view.findViewById(R.id.texto_descripcion_breve_del_curso);
        TextView fecha = (TextView) view.findViewById(R.id.texto_fecha_del_curso);
        TextView estado = (TextView) view.findViewById(R.id.texto_estado_del_curso);

        titulo.setText(curso.getTitulo());
        switch (curso.getNumImagen()){
            case 1 : imagen.setImageResource(R.drawable.diagnostico);
                break;
            case 2 : imagen.setImageResource(R.drawable.reparacion);
                break;
            case 3 : imagen.setImageResource(R.drawable.electronica);
                break;
            case 4 : imagen.setImageResource(R.drawable.programacion);
                break;
            default : imagen.setImageResource(R.drawable.reparacion);
                break;
        }
        descripcionBreve.setText(curso.getDescripcionBreve());
        fecha.setText(curso.getFechaInicio(true));
        estado.setText(curso.getEstado());

        if (notificacones) {
            estado.setVisibility(View.GONE);
        } else {
            estado.setVisibility(View.VISIBLE);
            if (estado.getText().toString().equals("Abierto")) {
                estado.setTextColor(view.getResources().getColor(R.color.colorTextGreen));
            } else {
                estado.setTextColor(view.getResources().getColor(R.color.colorTextRed));
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, YourCoursesDetailsActivity.class);
                intent.putExtra("objectData", curso);
                intent.putExtra("tipoDeUsuario", tipoDeUsuario);
                context.startActivity(intent);
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
