package com.adancruz.cedehaaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
    EditText nombre, apellidoPaterno, apellidoMaterno, correo, contrasena,
            conf_contrasena, numero, prefijo;
    View focusView = null;

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
        numero = (EditText) findViewById(R.id.numero_telefonico);
        prefijo = (EditText) findViewById(R.id.prefijo_telefonico);

        boton_aceptarRegistro = (Button) findViewById(R.id.boton_aceptar_registro);
        boton_aceptarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cancel = false;
                TextView[] campos = new TextView[8];
                campos[0] = nombre;
                campos[1] = apellidoPaterno;
                campos[2] = apellidoMaterno;
                campos[3] = correo;
                campos[4] = contrasena;
                campos[5] = conf_contrasena;
                campos[6] = prefijo;
                campos[7] = numero;

                if (isEmpty(campos) || containComilla(campos) || containSpace(campos)) {
                    focusView.requestFocus();
                } else {
                    if (!isEmailValid(correo.getText().toString())) {
                        correo.setError(getString(R.string.error_correo_invalido));
                        focusView = correo;
                        cancel = true;
                    }
                    if (!isPasswordValid(contrasena.getText().toString())) {
                        contrasena.setError(getString(R.string.error_contrasena_invalida));
                        focusView = contrasena;
                        cancel = true;
                    } else if (!(conf_contrasena.getText().toString().equals(contrasena.getText().toString()))) {
                        conf_contrasena.setError(getString(R.string.verificar_contrasena));
                        focusView = conf_contrasena;
                        cancel = true;
                    }
                    if ( !isPrefixValid(prefijo.getText().toString()) ||
                            isNotNumberValid(prefijo.getText().toString()) ) {
                        prefijo.setError(getString(R.string.prefijo_invalido));
                        focusView = prefijo;
                        cancel = true;
                    }
                    if ( !isPhoneNumberValid(numero.getText().toString()) ||
                            isNotNumberValid(numero.getText().toString()) ) {
                        numero.setError(getString(R.string.telefono_invalido));
                        focusView = numero;
                        cancel = true;
                    }
                    if (cancel) {
                        focusView.requestFocus();
                    } else {
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
                                Toast.makeText(StudentRegisterActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    };

                    String telefono = prefijo.getText().toString() + numero.getText().toString();
                    StudentRegisterRequest registerRequest = new StudentRegisterRequest(
                            nombre.getText().toString(),
                            apellidoPaterno.getText().toString(),
                            apellidoMaterno.getText().toString(),
                            correo.getText().toString(),
                            contrasena.getText().toString(),
                            telefono,
                            "estudiante",
                            "",
                            responseListener
                    );
                    RequestQueue queue = Volley.newRequestQueue(StudentRegisterActivity.this);
                    queue.add(registerRequest);
                    }
                }
            }
        });

        texto_leerTerminosYCondiciones = (TextView) findViewById(R.id.terminos_y_condiciones);
        texto_leerTerminosYCondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StudentRegisterActivity.this,
                        "Abriendo...",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isPasswordValid(String password) {
        //TODO: Reemplaza esto con tu propia lógica
        return password.length() > 4;
    }

    private boolean isEmailValid(String email) {
        //TODO: Reemplaza esto con tu propia lógica
        return email.contains("@");
    }

    private boolean isNotNumberValid(String string) {
        try {
            Integer.parseInt(string);
            return false;
        } catch (NumberFormatException nfe) {
            return true;
        }
    }

    private boolean isPrefixValid(String prefix) {
        return (prefix.length() >= 2 && prefix.length() <= 3);
    }

    private boolean isPhoneNumberValid(String phone) {
        return phone.length() == 7;
    }

    private boolean isEmpty(TextView[] campos) {
        boolean cancel = false;
        for (int i = 0; i < campos.length; i++) {
            if ( TextUtils.isEmpty(campos[i].getText().toString()) ) {
                campos[i].setError(getString(R.string.error_campo_requerido));
                focusView = campos[i];
                cancel = true;
            }
        }
        return cancel;
    }

    private boolean containSpace(TextView[] campos) {
        boolean cancel = false;
        for (int i = 1; i < campos.length; i++) {
            if (campos[i].getText().toString().contains(" ")) {
                campos[i].setError(getString(R.string.espacios));
                focusView = campos[i];
                cancel = true;
            }
        }
        return cancel;
    }

    private boolean containComilla(TextView[] campos){
        boolean cancel = false;
        for (int i = 0; i < campos.length; i++) {
            if (i != 5) {
                if (campos[i].getText().toString().contains("'")) {
                    campos[i].setError(getString(R.string.comilla_simple));
                    focusView = campos[i];
                    cancel = true;
                }
            }
        }
        return cancel;
    }
}