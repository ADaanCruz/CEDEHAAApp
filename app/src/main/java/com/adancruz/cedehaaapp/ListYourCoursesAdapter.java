package com.adancruz.cedehaaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListYourCoursesAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context contexto;
    String[][] datos;
    int[] datosImg;

    public ListYourCoursesAdapter(Context contexto, String[][] datos, int[] datosImg) {
        this.contexto = contexto;
        this.datos = datos;
        this.datosImg = datosImg;
        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.item_your_courses_list, null);

        TextView titulo = (TextView) view.findViewById(R.id.texto_titulo_del_curso);
        TextView descripcion = (TextView) view.findViewById(R.id.texto_descripcion_breve_del_curso);
        ImageView imagen = (ImageView) view.findViewById(R.id.imagen_item_del_curso);

        titulo.setText(datos[position][0]);
        descripcion.setText(datos[position][1]);
        imagen.setImageResource(datosImg[position]);

        return view;
    }

    @Override
    public int getCount() {
        return datosImg.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
