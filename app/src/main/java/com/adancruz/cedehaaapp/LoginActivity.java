package com.adancruz.cedehaaapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Una pantalla de inicio de sesión que ofrece inicio de sesión por correo electrónico / contraseña.
 */
public class LoginActivity extends AppCompatActivity {

    //public static final String APPLICATION_ID = "1B74BEF9-EABA-5307-FF35-B6F26136E600";
    //public static final String API_KEY = "71714F01-50CA-AE13-FF9E-C0CB3671B600";
    //public static final String SERVER_URL = "https://api.backendless.com";

    public static final String MY_PREFS_FILENAME = "com.adancruz.cedehaaappp.User";

    /**
     * Un almacén de autenticación ficticio que contiene nombres de usuario y contraseñas conocidos
     * TODO: Eliminar después de conectarse a un sistema de autenticación real.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Realiza un seguimiento de la tarea de inicio de sesión para garantizar que podamos cancelarla si así lo solicita.
     */
    private UserLoginTask mAuthTask = null;

    // Referencias de la interfaz de usuario.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    LinearLayout mLoginView;
    Switch sesionAbierta;
    Button boton_registrarse;
    Button boton_iniciarSesion;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mLoginView = findViewById(R.id.vista_login);
        sesionAbierta = (Switch) findViewById(R.id.switch_sesion_abierta);

        Drawable drawable = getResources().getDrawable(R.drawable.fondo2_2);
        mLoginView.setBackground(drawable);

        boton_registrarse = (Button) findViewById(R.id.boton_registrarse);
        boton_registrarse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, StudentRegisterActivity.class);
                startActivity(intent);
            }
        });

        boton_iniciarSesion = (Button) findViewById(R.id.boton_iniciar_sesion);
        boton_iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * Intenta iniciar sesión o registrar la cuenta especificada por el formulario de inicio de sesión
     * Si hay errores de formulario (correo electrónico no válido, campos faltantes, etc.),
     * se presentan los errores y no se realiza ningún intento de inicio de sesión real.
     */

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Restablecer errores.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Almacenar valores en el momento del intento de inicio de sesión.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Comprueba si hay una dirección de correo electrónico válida.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_campo_requerido));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_correo_invalido));
            focusView = mEmailView;
            cancel = true;
        } else
            // Comprueba si hay una contraseña válida.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_campo_requerido));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_contrasena_invalida));
            focusView = mPasswordView;
            cancel = true;
        } else {
            showProgress(true);
            cancel = false;
        }

        if (cancel) {
            // Hubo un error; no intenta iniciar sesión y enfoca el
            // primer campo de formulario con un error.
            focusView.requestFocus();
        } else {
            // Muestra un marcador de progreso y comienza una tarea en segundo plano
            // para realizar el intento de inicio de sesión del usuario.
            String correo = mEmailView.getText().toString();
            String contrasena = mPasswordView.getText().toString();

            loginWithBD(correo, contrasena);
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
                        showProgress(false);
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
                                    "Acceso fallido, verifica los campos",
                                    Toast.LENGTH_LONG).show();
                        }
                        showProgress(false);
                    }
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,
                            "ERROR: "+e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    showProgress(false);
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
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE).edit();
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
        //TODO: Reemplaza esto con tu propia lógica
        return email.contains("@");
        //return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Reemplaza esto con tu propia lógica
        return password.length() > 4;
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


    /**
     * Representa una tarea de inicio de sesión / registro asíncrono
     * utilizada para autenticar al usuario.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: Intento de autenticación contra un servicio de red.

            try {
                // Simular el acceso a la red.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // La cuenta existe, devuelve verdadero si la contraseña coincide.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: Registre la nueva cuenta aquí.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
                //finish();
                //startActivity(intent);
            } else {
                mPasswordView.setError(getString(R.string.error_contrasena_incorrecta));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

