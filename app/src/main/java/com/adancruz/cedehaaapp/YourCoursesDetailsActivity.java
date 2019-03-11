package com.adancruz.cedehaaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class YourCoursesDetailsActivity extends AppCompatActivity {

    private Curso item;
    private TextView titulo, descripcionBreve, descripcionGeneral, fechaInicio;
    private ImageView imagen;
    private Button button;
    private String tipoDeUsuario = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_yc_details_list);

        item = (Curso) getIntent().getSerializableExtra("objectData");

        titulo = (TextView) findViewById(R.id.texto_titulo_de_tu_curso_details);
        imagen = (ImageView) findViewById(R.id.imagen_de_tu_curso_details);
        descripcionBreve = (TextView) findViewById(R.id.texto_desc_breve_del_curso_details);
        descripcionGeneral = (TextView) findViewById(R.id.texto_desc_gral_del_curso_details);
        fechaInicio = (TextView) findViewById(R.id.texto_fecha_inicio_yc_details);
        button = (Button) findViewById(R.id.boton_detalle_de_curso);

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
        descripcionGeneral.setText(item.getDescripcionGeneral());
        fechaInicio.setText(item.getFechaInicio(true));

        tipoDeUsuario = this.getIntent().getStringExtra("tipoDeUsuario");

        String textoBoton;
        if (tipoDeUsuario.equals("administrador")) {
            textoBoton = "Editar curso";
            button.setText(textoBoton);
        } else if (tipoDeUsuario.equals("estudiante")){
            textoBoton = "Estudiante";
            button.setText(textoBoton);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(YourCoursesDetailsActivity.this, CreateCourseActivity.class);
                String txtBoton = button.getText().toString();
                if (txtBoton.equals("Editar curso")) {
                    intent.putExtra("editar", true);
                    intent.putExtra("titulo", item.getTitulo());
                    intent.putExtra("imagen", item.getNumImagen());
                    intent.putExtra("descBreve", item.getDescripcionBreve());
                    intent.putExtra("descGeneral", item.getDescripcionGeneral());
                    intent.putExtra("fechaInicio", item.getFechaInicio(false));
                    intent.putExtra("limiteEstudiantes", item.getLimiteEstudiantes());
                    intent.putExtra("estado", item.getEstado());
                    startActivity(intent);
                    finish();
                } else if (txtBoton.equals("Estudiante")) {
                    intent.putExtra("editar", false);
                }
            }
        });

    }
}
