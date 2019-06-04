package com.adancruz.cedehaaapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateCourseActivity extends AppCompatActivity {

    private boolean selectionDialog;

    private static final String TAG = "CreateCourseActivity";
    private TextView titulo, descripcionBreve, descripcionGeneral, limiteEstudiantes, cambios;
    String exTitulo, exFechaInicio, exDescBreve;
    public CalendarView calendarioFechaInicio;
    private ImageView imagen;
    public Button aceptarCursoNuevo;
    private ToggleButton estadoCurso;
    private ProgressBar mProgressView;
    View focusView = null;
    int numImagen;
    String fechaInicio = "";
    String estado = "Abierto";

    Response.Listener<String> responseListener;
    private static String url;

    private ConnectivityManager con;
    private NetworkInfo network;
    private AlertDialog.Builder dialog;
    private boolean internet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_course);

        // Componentes bàsico de XML.
        titulo = findViewById(R.id.texto_titulo_del_curso_nuevo);
        imagen = findViewById(R.id.imagen_del_curso_nuevo);
        descripcionBreve = findViewById(R.id.texto_desc_breve_del_curso_nuevo);
        descripcionGeneral = findViewById(R.id.texto_desc_gral_del_curso_nuevo);
        limiteEstudiantes = findViewById(R.id.texto_limite_estudiantes);
        calendarioFechaInicio = findViewById(R.id.calendario_inicio_curso);
        estadoCurso = findViewById(R.id.toggle_estado_curso);
        aceptarCursoNuevo = findViewById(R.id.boton_aceptar_curso_nuevo);
        cambios = findViewById(R.id.texto_proximos_cambios);
        mProgressView = findViewById(R.id.progressBar_create_curso);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch ((numImagen+1)){
                    case 1: imagen.setImageResource(R.drawable.diagnostico); numImagen=1;
                        break;
                    case 2: imagen.setImageResource(R.drawable.reparacion); numImagen=2;
                        break;
                    case 3: imagen.setImageResource(R.drawable.electronica); numImagen=3;
                        break;
                    case 4: imagen.setImageResource(R.drawable.programacion); numImagen=4;
                        break;
                    default: imagen.setImageResource(R.drawable.diagnostico); numImagen=1;
                        break;
                }
            }
        });

        calendarioFechaInicio.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                fechaInicio = year + "-" + (month+1) + "-" + day;
                Log.d(TAG, "onSelectedDayChange: yyyy-mm-dd: " + fechaInicio);
            }
        });

        estadoCurso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    estado = "Abierto";
                } else {
                    estado = "Cerrado";
                }
            }
        });

        // Identificar si es "editar curso" o "crear curso".
        final boolean editar = getIntent().getBooleanExtra("editar", true);
        if (editar) {
            cambios.setVisibility(View.GONE);
            aceptarCursoNuevo.setText(getString(R.string.aceptar_cambios));

            // Guardar valores para buscar en la base de datos.
            Intent intent = getIntent();
            exTitulo = intent.getStringExtra("titulo");
            exDescBreve = intent.getStringExtra("descBreve");
            exFechaInicio = intent.getStringExtra("fechaInicio");

            // Obtener los valores para mostrarlos y editarlos.
            titulo.setText(exTitulo);
            switch (intent.getIntExtra("imagen", 1)) {
                case 1:
                    imagen.setImageResource(R.drawable.diagnostico); numImagen = 1;
                    break;
                case 2:
                    imagen.setImageResource(R.drawable.reparacion); numImagen = 2;
                    break;
                case 3:
                    imagen.setImageResource(R.drawable.electronica); numImagen = 3;
                    break;
                case 4:
                    imagen.setImageResource(R.drawable.programacion); numImagen = 4;
                    break;
            }
            descripcionBreve.setText(exDescBreve);
            descripcionGeneral.setText(intent.getStringExtra("descGeneral"));
            fechaInicio = exFechaInicio;
            String limite = intent.getIntExtra("limiteEstudiantes", 20) + "";
            limiteEstudiantes.setText(limite);

            responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            Toast.makeText(CreateCourseActivity.this,
                                    "Curso editado correctamente",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            String error = jsonObject.getString("message");
                            String select = "select";
                            if (error.equals(select)) {
                                Toast.makeText(CreateCourseActivity.this,
                                        "Error del servidor",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(CreateCourseActivity.this,
                                        "Error del servidor",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(CreateCourseActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            };
            url = DireccionesURL.EDIT_COURSE_REQUEST_URL;
        } else {
            cambios.setVisibility(View.VISIBLE);
            aceptarCursoNuevo.setText(R.string.aceptar_curso_nuevo);
            numImagen = 0;

            responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            Toast.makeText(CreateCourseActivity.this,
                                    "Curso creado correctamente",
                                    Toast.LENGTH_LONG).show();
                            mandarNotificaciones();
                            finish();
                        } else {
                            String error = jsonObject.getString("message");
                            String post = "post", insert = "insert";
                            if (error.equals(post)) {
                                Toast.makeText(CreateCourseActivity.this,
                                        "Error con el servidor (POST)",
                                        Toast.LENGTH_LONG).show();
                            } else if (error.equals(insert)) {
                                Toast.makeText(CreateCourseActivity.this,
                                        "Error con el servidor (SQL)",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(CreateCourseActivity.this,
                                        "Error con el servidor",
                                        Toast.LENGTH_LONG).show();
                            }
                            showProgress(false);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(CreateCourseActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        showProgress(false);
                    }
                }
            };
            url = DireccionesURL.NEW_COURSE_REQUEST_URL;
        }

        aceptarCursoNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                internet = verifyInternet(v.getContext());
                if (internet){
                    boolean cancel;
                    cancel = validarCampos();

                    if (!cancel) {
                        String numeroDeImagen = numImagen + "";
                        CreateCourseRequest courseRequest = new CreateCourseRequest(
                                editar,
                                url,
                                exTitulo,
                                exDescBreve,
                                exFechaInicio,

                                titulo.getText().toString(),
                                numeroDeImagen,
                                descripcionBreve.getText().toString(),
                                descripcionGeneral.getText().toString(),
                                fechaInicio,
                                limiteEstudiantes.getText().toString(),
                                estado,
                                responseListener
                        );
                        RequestQueue queue = Volley.newRequestQueue(CreateCourseActivity.this);
                        queue.add(courseRequest);
                    } else {
                        showProgress(false);
                    }
                } else {
                    mostrarDialog(
                            v.getContext().getString(R.string.verify_internet_dialog_message),
                            v.getContext().getString(R.string.verify_internet_dialog_title),
                            "Entendido",
                            "Configuración",
                            v.getContext(),
                            "wifi"
                    );
                    showProgress(false);
                }
            }
        });
    }

    private boolean validarCampos() {
        boolean cancel = false;
        TextView[] campos = new TextView[4];
        campos[0] = titulo;
        campos[1] = descripcionBreve;
        campos[2] = descripcionGeneral;
        campos[3] = limiteEstudiantes;

        if (isEmpty(campos) || containSpace(campos) || containComilla(campos)) {
            focusView.requestFocus();
            cancel = true;
        } else {
            if (isNotNumberValid(campos[3].getText().toString())) {
                cancel = true;
                limiteEstudiantes.setError(getString(R.string.numero_invalido));
                focusView = limiteEstudiantes;
            } else if (numImagen == (0)) {
                cancel = true;
                Toast.makeText(CreateCourseActivity.this,
                        "Selecciona una imagen",
                        Toast.LENGTH_LONG).show();
            } else if (fechaInicio.isEmpty()) {
                cancel = true;
                Toast.makeText(CreateCourseActivity.this,
                        "Selecciona una fecha",
                        Toast.LENGTH_LONG).show();
            }
        }

        return cancel;
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
                campo.setError(getString(R.string.no_vacio));
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

    private boolean isNotNumberValid(String string) {
        if (string.length() <= 2) {
            try {
                return Integer.parseInt(string) <= 0;
            } catch (NumberFormatException nfe) {
                return true;
            }
        } else {
            return true;
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
                        Toast.makeText(CreateCourseActivity.this,
                                "Notificación enviada",
                                Toast.LENGTH_LONG).show();
                    } else {
                        String error = jsonObject.getString("message");
                        String post = "post", tokens = "tokens";
                        if (error.equals(post)) {
                            Toast.makeText(CreateCourseActivity.this,
                                    "Error: Type POST",
                                    Toast.LENGTH_LONG).show();
                        } else if (error.equals(tokens)) {
                            Toast.makeText(CreateCourseActivity.this,
                                    "Error con los tokens de los usuarios",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CreateCourseActivity.this,
                                    "Notificación no enviada",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(CreateCourseActivity.this,
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
        RequestQueue queue = Volley.newRequestQueue(CreateCourseActivity.this);
        queue.add(notificacionesRequest);
    }

    private boolean verifyInternet(Context context) {
        con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        network = con.getActiveNetworkInfo();
        return network != null && network.isConnected();
    }

    private void mostrarDialog(String mensaje, String titulo, String positive, String negative, final Context context, final String metodo){
        dialog = new AlertDialog.Builder(context);
        dialog.setMessage(mensaje)
                .setTitle(titulo);
        dialog.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectionDialog = true;

            }
        });

        if (negative != null) {
            dialog.setNegativeButton(negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectionDialog = false;
                    /*switch (metodo) {
                        case "wifi": context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            break;
                        default:
                            break;
                    }*/
                    if (metodo.equals("wifi")) {
                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }
            });
        }
        dialog.show();
    }

    private void showProgress(boolean show) {
        if (show)
        {
            aceptarCursoNuevo.setVisibility(View.GONE);
            mProgressView.setVisibility(View.VISIBLE);
        } else {
            aceptarCursoNuevo.setVisibility(View.VISIBLE);
            mProgressView.setVisibility(View.GONE);
        }
    }
}
