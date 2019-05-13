package com.adancruz.cedehaaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class StudentRegisterActivity extends AppCompatActivity {

    public static final String MY_PREFS_FILENAME = "com.adancruz.cedehaaappp.User";
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    private Button boton_aceptarRegistro;
    private TextView registro_telefono, texto_leerTerminosYCondiciones;
    private EditText nombre, apellidoPaterno, apellidoMaterno, correo, contrasena,
            conf_contrasena, numero, prefijo, codigo_registro;
    private RadioGroup tipo_registro;
    private View focusView = null;

    private TextView[] campos;
    private String tipoDeUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        tipo_registro = findViewById(R.id.tipo_de_registro);
        codigo_registro = findViewById(R.id.codigo_registro);

        nombre = findViewById(R.id.nombre);
        apellidoPaterno = findViewById(R.id.apellidoPaterno);
        apellidoMaterno = findViewById(R.id.apellidoMaterno);
        correo = findViewById(R.id.correo_reg);
        contrasena = findViewById(R.id.contrasena_reg);
        conf_contrasena = findViewById(R.id.conf_contrasena_reg);
        registro_telefono = findViewById(R.id.registro_telefono);
        prefijo = findViewById(R.id.prefijo_telefonico);
        numero = findViewById(R.id.numero_telefonico);
        boton_aceptarRegistro = findViewById(R.id.boton_aceptar_registro);
        texto_leerTerminosYCondiciones = findViewById(R.id.terminos_y_condiciones);

        tipo_registro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                vistaRegistro(radioButton.getText().toString());
            }
        });

        boton_aceptarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cancel = false;

                tipoDeUsuario = obtenerTipoRegistro();
                cargarCampos(tipoDeUsuario);
                if (campos == null) {
                    Toast.makeText(StudentRegisterActivity.this, "Selecciona un tipo de registro", Toast.LENGTH_LONG).show();
                } else if (isEmpty(campos) || containComilla(campos) || containSpace(campos)) {
                    focusView.requestFocus();
                } else {
                    if (tipoDeUsuario.equals("administrador")) {
                        if (!codigo_registro.getText().toString().equals("c0d1g0")) {
                            codigo_registro.setError(getString(R.string.incorrecto));
                            focusView = codigo_registro;
                            cancel = true;
                        }
                    }
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
                    if (tipoDeUsuario.equals("estudiante")) {
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
                                        guardarInfoBasica(tipoDeUsuario);
                                        finish();
                                        startActivity(new Intent(StudentRegisterActivity.this, StudentActivity.class));

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

                        String telefono = "";
                        if (tipoDeUsuario.equals("estudiante")) {
                            telefono = prefijo.getText().toString() + numero.getText().toString();
                        }
                        prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);
                        StudentRegisterRequest registerRequest = new StudentRegisterRequest(
                                nombre.getText().toString(),
                                apellidoPaterno.getText().toString(),
                                apellidoMaterno.getText().toString(),
                                correo.getText().toString(),
                                contrasena.getText().toString(),
                                telefono,
                                tipoDeUsuario,
                                "",
                                prefs.getString("token","nothing"),
                                responseListener
                        );
                        RequestQueue queue = Volley.newRequestQueue(StudentRegisterActivity.this);
                        queue.add(registerRequest);
                    }
                }
            }
        });

        texto_leerTerminosYCondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StudentRegisterActivity.this,
                        "Abriendo...",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarInfoBasica(String tipoDeUsuario) {
        editor = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE).edit();

        editor.putString("nombre", nombre.getText().toString());
        editor.putString("apellidoPaterno", apellidoPaterno.getText().toString());
        editor.putString("apellidoMaterno", apellidoMaterno.getText().toString());
        editor.putString("correo", correo.getText().toString());
        editor.putString("contrasena", contrasena.getText().toString());
        if (tipoDeUsuario.equals("estudiante")) {
            editor.putString("telefono", (prefijo.getText().toString() + numero.getText().toString()));
        }
        editor.putString("tipoDeUsuario", tipoDeUsuario);
        editor.putBoolean("first", false);
        editor.putBoolean("infoBasica", true);
        editor.putBoolean("sesion", true);

        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(StudentRegisterActivity.this, LoginActivity.class));
    }

    private String obtenerTipoRegistro() {
        String tipoRegistro;
        switch (tipo_registro.getCheckedRadioButtonId()) {
            case R.id.radioBtn_est:
                tipoRegistro = "estudiante";
                break;
            case R.id.radioBtn_adm:
                tipoRegistro = "administrador";
                break;
            default:
                tipoRegistro = "none";
                break;
        }
        return tipoRegistro;
    }

    private void vistaRegistro(String tipo_de_usuario) {
        switch (tipo_de_usuario){
            case "Estudiante":
                codigo_registro.setVisibility(View.GONE);

                registro_telefono.setVisibility(View.VISIBLE);
                prefijo.setVisibility(View.VISIBLE);
                numero.setVisibility(View.VISIBLE);

                boton_aceptarRegistro.setVisibility(View.VISIBLE);
                texto_leerTerminosYCondiciones.setVisibility(View.VISIBLE);
                break;
            case "Administrador":
                codigo_registro.setVisibility(View.VISIBLE);

                registro_telefono.setVisibility(View.GONE);
                prefijo.setVisibility(View.GONE);
                numero.setVisibility(View.GONE);

                boton_aceptarRegistro.setVisibility(View.VISIBLE);
                texto_leerTerminosYCondiciones.setVisibility(View.VISIBLE);
                break;
            default:
                boton_aceptarRegistro.setVisibility(View.GONE);
                texto_leerTerminosYCondiciones.setVisibility(View.GONE);
                break;
        }
    }

    private void cargarCampos(String tipoDeUsuario) {
        switch (tipoDeUsuario) {
            case "estudiante":
                campos = new TextView[8];
                campos[0] = nombre;
                campos[1] = apellidoPaterno;
                campos[2] = apellidoMaterno;
                campos[3] = correo;
                campos[4] = contrasena;
                campos[5] = conf_contrasena;
                campos[6] = prefijo;
                campos[7] = numero;
                break;
            case "administrador":
                campos = new TextView[7];
                campos[0] = codigo_registro;
                campos[1] = nombre;
                campos[2] = apellidoPaterno;
                campos[3] = apellidoMaterno;
                campos[4] = correo;
                campos[5] = contrasena;
                campos[6] = conf_contrasena;
                break;
            case "none":
                campos = null;
            default:
                campos = null;
                break;
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isEmailValid(String email) {
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
        for (TextView campo : campos) {
            if (TextUtils.isEmpty(campo.getText().toString())) {
                campo.setError(getString(R.string.error_campo_requerido));
                focusView = campo;
                cancel = true;
            }
        }
        return cancel;
    }

    private boolean containSpace(TextView[] campos) {
        boolean cancel = false;
        for (TextView campo : campos) {
            if (campo != nombre) {
                if (campo.getText().toString().contains(" ")) {
                    campo.setError(getString(R.string.espacios));
                    focusView = campo;
                    cancel = true;
                }
            }
        }
        return cancel;
    }

    private boolean containComilla(TextView[] campos){
        boolean cancel = false;
        for (TextView campo : campos) {
            if (campo.getText().toString().contains("'")) {
                campo.setError(getString(R.string.comilla_simple));
                focusView = campo;
                cancel = true;
            }
        }
        return cancel;
    }
}