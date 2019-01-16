package com.adancruz.cedehaaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StudentRegisterActivity extends AppCompatActivity {

    Button boton_aceptarRegistro;
    TextView texto_leerTerminosYCondiciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        boton_aceptarRegistro = (Button) findViewById(R.id.boton_aceptar_registro);
        boton_aceptarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        texto_leerTerminosYCondiciones = (TextView) findViewById(R.id.terminos_y_condiciones);
        texto_leerTerminosYCondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StudentRegisterActivity.this, "Abriendo...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
