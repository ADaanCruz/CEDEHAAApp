package com.adancruz.cedehaaapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CreateCourseActivity extends AppCompatActivity {

    private TextView titulo, descripcionBreve, getDescripcionGeneral;
    private ImageView imagen;
    private Button aceptarCursoNuevo;
    int numImagen = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_course);

        titulo = (TextView) findViewById(R.id.texto_titulo_del_curso_nuevo);
        descripcionBreve = (TextView) findViewById(R.id.texto_desc_breve_del_curso_nuevo);
        getDescripcionGeneral = (TextView) findViewById(R.id.texto_desc_gral_del_curso_nuevo);
        imagen = (ImageView) findViewById(R.id.imagen_del_curso_nuevo);
        aceptarCursoNuevo = (Button) findViewById(R.id.boton_aceptar_curso_nuevo);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (numImagen){
                    case 1: imagen.setImageResource(R.drawable.diagnostico); numImagen+=1;
                        break;
                    case 2: imagen.setImageResource(R.drawable.reparacion); numImagen+=1;
                        break;
                    case 3: imagen.setImageResource(R.drawable.electronica); numImagen+=1;
                        break;
                    case 4: imagen.setImageResource(R.drawable.programacion); numImagen=1;
                        break;
                }
            }
        });

        aceptarCursoNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
