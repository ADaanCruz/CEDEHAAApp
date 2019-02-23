package com.adancruz.cedehaaapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class YourCoursesDetailsActivity extends AppCompatActivity {

    private Curso item;
    private TextView titulo, descripcionBreve, getDescripcionGeneral;
    private ImageView imagen;
    private Button notificaciones;

    private String tipoDeUsuario = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_yc_details_list);

        item = (Curso) getIntent().getSerializableExtra("objectData");

        titulo = (TextView) findViewById(R.id.texto_titulo_de_tu_curso_details);
        imagen = (ImageView) findViewById(R.id.imagen_de_tu_curso_details);
        descripcionBreve = (TextView) findViewById(R.id.texto_desc_breve_del_curso_details);
        getDescripcionGeneral = (TextView) findViewById(R.id.texto_desc_gral_del_curso_details);

        notificaciones = (Button) findViewById(R.id.boton_notificaciones_de_tu_curso);

        titulo.setText(item.getTitulo());
        switch (item.getNumImagen()){
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
        descripcionBreve.setText(item.getDescripcionBreve());
        getDescripcionGeneral.setText(item.getDescripcionGeneral());

        tipoDeUsuario = this.getIntent().getStringExtra("tipoDeUsuario");
    }
}
