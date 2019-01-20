package com.adancruz.cedehaaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class StudentRegisterActivity extends AppCompatActivity {

    Button boton_aceptarRegistro;
    TextView texto_leerTerminosYCondiciones;
    EditText nombre, apellidoPaterno, apellidoMaterno, correo, contrasena, conf_contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        nombre = (EditText) findViewById(R.id.nombre);
        apellidoPaterno = (EditText) findViewById(R.id.apellidoPaterno);
        apellidoMaterno = (EditText) findViewById(R.id.apellidoMaterno);
        correo = (EditText) findViewById(R.id.correo_reg);
        contrasena = (EditText) findViewById(R.id.contrasena_reg);
        conf_contrasena = (EditText) findViewById(R.id.conf_contrasena_reg);

        boton_aceptarRegistro = (Button) findViewById(R.id.boton_aceptar_registro);
        boton_aceptarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombres = nombre.getText().toString();
                String apellido_paterno = apellidoPaterno.getText().toString();
                String apellido_materno = apellidoMaterno.getText().toString();
                String correo_electronico = correo.getText().toString();
                String contrasena_ = contrasena.getText().toString();
                String conf_contrasena_ = conf_contrasena.getText().toString();

                if (conf_contrasena_.equals(contrasena_)) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    Toast.makeText(StudentRegisterActivity.this,
                                            "¡Registro realizado!", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    String error = jsonObject.getString("message");
                                    String existing = "existing", post = "post";
                                    if (error.equals(existing)) {
                                        Toast.makeText(StudentRegisterActivity.this,
                                                "El correo ya está registrado",
                                                Toast.LENGTH_LONG).show();
                                    } else if (error.equals(post)) {
                                        Toast.makeText(StudentRegisterActivity.this,
                                                "Error: Type POST",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(StudentRegisterActivity.this,
                                                "Algo salió mal",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                Toast.makeText(StudentRegisterActivity.this, "ERROR: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    };

                    StudentRegisterRequest registerRequest = new StudentRegisterRequest(
                            nombres, apellido_paterno, apellido_materno, correo_electronico, contrasena_,
                            "estudiante", "", responseListener
                    );
                    RequestQueue queue = Volley.newRequestQueue(StudentRegisterActivity.this);
                    queue.add(registerRequest);
                } else {
                    Toast.makeText(StudentRegisterActivity.this, "Confirma la contrasena",
                            Toast.LENGTH_LONG).show();
                }
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
