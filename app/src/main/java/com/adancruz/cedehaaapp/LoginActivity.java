package com.adancruz.cedehaaapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Una pantalla de inicio de sesión que ofrece inicio de sesión por correo electrónico / contraseña.
 */
public class LoginActivity extends AppCompatActivity {

    public static final String MY_PREFS_FILENAME = "com.adancruz.cedehaaappp.User";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    // Referencias de la interfaz de usuario.
    private ImageView logo;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    LinearLayout mLoginView;
    Switch sesionAbierta;
    Button boton_registrarse;
    Button boton_iniciarSesion;
    TextView olvidaste;

    private TextView bien_introduccion, introduccion, titulo_login;

    View focusView = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);
        //editor = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE).edit();

        bien_introduccion = findViewById(R.id.texto_bienvenida_introduccion);
        introduccion = findViewById(R.id.texto_introduccion);

        titulo_login = findViewById(R.id.texto_titulo_login);
        logo = findViewById(R.id.logo_login);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mLoginView = findViewById(R.id.vista_login);
        sesionAbierta = findViewById(R.id.switch_sesion_abierta);
        olvidaste = findViewById(R.id.olvidaste);

        boton_registrarse = findViewById(R.id.boton_registrarse);
        boton_iniciarSesion = findViewById(R.id.boton_iniciar_sesion);

        boton_registrarse.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginActivity.this, StudentRegisterActivity.class));
            }
        });

        boton_iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                // Para pruebas se deja en true, pero sin poder iniciar sesión.
                if (prefs.getBoolean("first", true)) {
                    //editor.putBoolean("first", false);
                    //editor.apply();
                    cargarFondo();
                    showLoginForm(true);
                } else {
                    showProgress(true);
                    boton_iniciarSesion.setEnabled(false);
                    attemptLogin();
                    boton_iniciarSesion.setEnabled(true);
                }
            }
        });

        olvidaste.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mandarNotificaciones();
            }
        });

        if (prefs.getBoolean("first", true)) {
            showLoginForm(false);
        } else {
            cargarFondo();
            showLoginForm(true);
        }
    }

    /**
     * Intenta iniciar sesión o registrar la cuenta especificada por el formulario de inicio de sesión
     * Si hay errores de formulario (correo electrónico no válido, campos faltantes, etc.),
     * se presentan los errores y no se realiza ningún intento de inicio de sesión real.
     */

    private void attemptLogin() {
        // Restablecer errores.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        boolean cancel = false;
        // Almacenar valores en el momento del intento de inicio de sesión.
        TextView[] campos = new TextView[2];
        campos[0] = mEmailView;
        campos[1] = mPasswordView;

        // Comprueba si hay una dirección de correo electrónico válida.
        if (isEmpty(campos) || containSpace(campos) || containComilla(campos)) {
            focusView.requestFocus();
            showProgress(false);
        } else {
            String correo = mEmailView.getText().toString();
            String contrasena = mPasswordView.getText().toString();

            if (!isEmailValid(correo)) {
                mEmailView.setError(getString(R.string.error_correo_invalido));
                focusView = mEmailView;
                focusView.requestFocus();
                cancel = true;
            }
            // Comprueba si hay una contraseña válida.
            if (!isPasswordValid(contrasena)) {
                mPasswordView.setError(getString(R.string.error_contrasena_invalida));
                focusView = mPasswordView;
                focusView.requestFocus();
                cancel = true;
            }

            if (cancel) {
                // Hubo un error y enfoca el campo con un error.
                focusView.requestFocus();
            } else {
                // Muestra un marcador de progreso y comienza una tarea en segundo plano
                // para realizar el intento de inicio de sesión del usuario.
                loginWithBD(correo, contrasena);
            }
            showProgress(false);
        }
    }

    private void loginWithBD(final String correo, final String contrasena) {
        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);

                        String nombre = jsonObject.getString("nombre");
                        String apellidoPaterno = jsonObject.getString("apellidoPaterno");
                        String apellidoMaterno = jsonObject.getString("apellidoMaterno");
                        String email = prefs.getString("correo", null);
                        String tipoDeUsuario = jsonObject.getString("tipoDeUsuario");
                        String telefono = "";
                        if (!tipoDeUsuario.equals("administrador")) {
                            telefono = jsonObject.getString("telefono");
                        }

                        boolean infoBasica = prefs.getBoolean("infoBasica", false);
                        guardarInfoBasica(nombre, apellidoPaterno, apellidoMaterno, correo, email,
                                        contrasena, tipoDeUsuario, telefono, infoBasica);

                        finish();
                        startActivity(new Intent(LoginActivity.this, StudentActivity.class));
                    } else {
                        String error = jsonObject.getString("message");
                        String password = "password", email = "email", post = "post";
                        if (error.equals(password)) {
                            Toast.makeText(LoginActivity.this,
                                    "La contraseña es incorrecta",
                                    Toast.LENGTH_LONG).show();
                        } else if (error.equals(email)) {
                            Toast.makeText(LoginActivity.this,
                                    "El correo no está registrado",
                                    Toast.LENGTH_LONG).show();
                        } else if (error.equals(post)) {
                            Toast.makeText(LoginActivity.this,
                                    "Error: Type POST",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Algo salió mal",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,
                            "ERROR: "+e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(correo, contrasena, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
    }

    private void guardarInfoBasica(String nombre, String apellidoPaterno, String apellidoMaterno,
                                   String correo, String email, String contrasena, String tipoDeUsuario,
                                   String telefono, boolean infoBasica) {
        if (!infoBasica || !email.equals(correo)) {
            editor.putString("nombre", nombre);
            editor.putString("apellidoPaterno", apellidoPaterno);
            editor.putString("apellidoMaterno", apellidoMaterno);
            editor.putString("correo", correo);
            editor.putString("contrasena", contrasena);
            if (!tipoDeUsuario.equals("administrador")) {
                editor.putString("telefono", telefono);
            }
            editor.putString("tipoDeUsuario", tipoDeUsuario);
            editor.putBoolean("infoBasica", true);
        }
        if (sesionAbierta.isChecked()) {
            editor.putBoolean("sesion", true);
        } else {
            editor.putBoolean("sesion", false);
        }

        editor.apply();
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
        //return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
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
            String vacio = campo.getText().toString();
            if (vacio.replace(" ", "").isEmpty()) {
                campo.setError(getString(R.string.espacios));
                focusView = campo;
                cancel = true;
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

    /**
     * Muestra el progreso de la interfaz de usuario y oculta el formulario de inicio de sesión.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // En Honeycomb MR2 tenemos las API ViewPropertyAnimator, que
        // permiten animaciones muy fáciles. Si está disponible, use
        // estas API para fundir el selector de progreso.

        if (show)
        {
            mLoginFormView.setVisibility(View.GONE);
            mProgressView.setVisibility(View.VISIBLE);
        } else {
            mLoginFormView.setVisibility(View.VISIBLE);
            mProgressView.setVisibility(View.GONE);
        }
    }

    private void mandarNotificaciones() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Toast.makeText(LoginActivity.this,
                                "Notificación enviada",
                                Toast.LENGTH_LONG).show();
                    } else {
                        String error = jsonObject.getString("message");
                        String post = "post", tokens = "tokens";
                        if (error.equals(post)) {
                            Toast.makeText(LoginActivity.this,
                                    "Error: Type POST",
                                    Toast.LENGTH_LONG).show();
                        } else if (error.equals(tokens)) {
                            Toast.makeText(LoginActivity.this,
                                    "Error con los tokens de los usuarios",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Notificación no enviada",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,
                            "ERROR: "+e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };

        NotificacionesRequest notificacionesRequest = new NotificacionesRequest(
                "Cursos nuevos",
                "Una nueva oportunidad para ti.",
                responseListener
        );
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(notificacionesRequest);
    }

    private void showLoginForm(boolean show) {
        if (show) {
            bien_introduccion.setVisibility(View.GONE);
            introduccion.setVisibility(View.GONE);

            titulo_login.setVisibility(View.VISIBLE);
            logo.setVisibility(View.VISIBLE);
            mLoginFormView.setVisibility(View.VISIBLE);

            olvidaste.setVisibility(View.VISIBLE);
        } else {
            bien_introduccion.setVisibility(View.VISIBLE);
            introduccion.setVisibility(View.VISIBLE);

            titulo_login.setVisibility(View.GONE);
            logo.setVisibility(View.GONE);
            mLoginFormView.setVisibility(View.GONE);

            olvidaste.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void cargarFondo() {
        Drawable drawable = getResources().getDrawable(R.drawable.fondo2_2);
        mLoginView.setBackground(drawable);
    }

}