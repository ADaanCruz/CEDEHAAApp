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

    public ListCoursesAdapter(Context context, ArrayList<Curso> listItems) {
        this.context = context;
        this.listItems = listItems;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.item_courses_list, null);
        final Curso curso = (Curso) getItem(position);

        ImageView imagen = (ImageView) view.findViewById(R.id.imagen_item_del_curso);
        TextView titulo = (TextView) view.findViewById(R.id.texto_titulo_del_curso);
        TextView descripcion = (TextView) view.findViewById(R.id.texto_descripcion_breve_del_curso);

        imagen.setImageResource(curso.getImagen());
        titulo.setText(curso.getTitulo());
        descripcion.setText(curso.getDescripcionBreve());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, YourCoursesDetailsActivity.class);
                intent.putExtra("objectData", curso);
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
