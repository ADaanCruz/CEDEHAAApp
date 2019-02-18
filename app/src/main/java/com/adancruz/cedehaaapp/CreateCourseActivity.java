package com.adancruz.cedehaaapp;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;

public class CreateCourseActivity extends AppCompatActivity {

    private static final String TAG = "CreateCourseActivity";
    private TextView titulo, descripcionBreve, descripcionGeneral, limiteEstudiantes;
    public CalendarView calendarioFechaInicio;
    private ImageView imagen;
    public Button aceptarCursoNuevo;
    View focusView = null;
    int numImagen = (-1);
    String fechaInicio = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_course);

        titulo = (TextView) findViewById(R.id.texto_titulo_del_curso_nuevo);
        imagen = (ImageView) findViewById(R.id.imagen_del_curso_nuevo);
        descripcionBreve = (TextView) findViewById(R.id.texto_desc_breve_del_curso_nuevo);
        descripcionGeneral = (TextView) findViewById(R.id.texto_desc_gral_del_curso_nuevo);
        limiteEstudiantes = (TextView) findViewById(R.id.texto_limite_estudiantes);
        calendarioFechaInicio = (CalendarView) findViewById(R.id.calendario_inicio_curso);
        aceptarCursoNuevo = (Button) findViewById(R.id.boton_aceptar_curso_nuevo);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (numImagen){
                    case (-1): imagen.setImageResource(R.drawable.diagnostico); numImagen=1;
                        break;
                    case 0: imagen.setImageResource(R.drawable.diagnostico); numImagen=1;
                        break;
                    case 1: imagen.setImageResource(R.drawable.reparacion); numImagen=2;
                        break;
                    case 2: imagen.setImageResource(R.drawable.electronica); numImagen=3;
                        break;
                    case 3: imagen.setImageResource(R.drawable.programacion); numImagen=0;
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

        aceptarCursoNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cancel = false;
                TextView[] campos = new TextView[4];
                campos[0] = titulo;
                campos[1] = descripcionBreve;
                campos[2] = descripcionGeneral;
                campos[3] = limiteEstudiantes;

                cancel = isEmpty(campos);

                /*if (!cancel && checkDate(campos)) {
                    cancel = true;
                }*/

                if (cancel) {
                    focusView.requestFocus();
                } else if(numImagen==(-1)){
                    cancel = true;
                    Toast.makeText(CreateCourseActivity.this,
                            "Selecciona una imagen",
                            Toast.LENGTH_LONG).show();
                } else if (fechaInicio.isEmpty()) {
                    cancel = true;
                    Toast.makeText(CreateCourseActivity.this,
                            "Selecciona una fecha",
                            Toast.LENGTH_LONG).show();
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    Toast.makeText(CreateCourseActivity.this,
                                            "Curso creado correctamente",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    String error = jsonObject.getString("message");
                                    String post = "post";
                                    if (error.equals(post)) {
                                        Toast.makeText(CreateCourseActivity.this,
                                                "Error: Type POST",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(CreateCourseActivity.this,
                                                "Algo salió mal",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                Toast.makeText(CreateCourseActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    };
                    String numeroDeImagen = numImagen + "";
                    CreateCourseRequest courseRequest = new CreateCourseRequest(
                            titulo.getText().toString(),
                            numeroDeImagen,
                            descripcionBreve.getText().toString(),
                            descripcionGeneral.getText().toString(),
                            fechaInicio,
                            limiteEstudiantes.getText().toString(),
                            responseListener
                    );
                    RequestQueue queue = Volley.newRequestQueue(CreateCourseActivity.this);
                    queue.add(courseRequest);
                }
            }
        });
    }

    private boolean isEmpty(TextView[] campos) {
        boolean cancel = false;
        for (int i = 0; i < campos.length; i++) {
            if (TextUtils.isEmpty(campos[i].getText().toString())) {
                campos[i].setError(getString(R.string.error_campo_requerido));
                focusView = campos[i];
                cancel = true;
            } else {
                if (cancel) {
                    cancel = true;
                } else {
                    cancel = false;
                }
            }
        }
        return cancel;
    }

    private boolean checkDate(TextView[] campos){
        boolean cancel = false;
        String anio = campos[4].getText().toString();
        String mes = campos[5].getText().toString();
        String dia = campos[6].getText().toString();
        int fechaDelSistema = 2019;
        Toast.makeText(CreateCourseActivity.this,
                ""+fechaDelSistema,
                Toast.LENGTH_LONG).show();
        if (anio.length() != 4) {
            campos[4].setError("Formato incorrecto (AAAA)");
            focusView = campos[4];
            cancel = true;
        } else if (Integer.parseInt(anio)<fechaDelSistema){
            campos[4].setError(fechaDelSistema + "en adelante");
            focusView = campos[4];
            cancel = true;
        }

        if (mes.length() != 2) {
            campos[5].setError("Formato incorrecto (MM)");
            focusView = campos[5];
            cancel = true;
        } else if (Integer.parseInt(mes)>12){
            campos[5].setError("Formato incorrecto (MM)");
            focusView = campos[5];
            cancel = true;
        }

        if (dia.length() != 2) {
            campos[6].setError("Formato incorrecto (DD)");
            focusView = campos[6];
            cancel = true;
        } else if (Integer.parseInt(dia)>31){
            campos[6].setError("Formato incorrecto (DD)");
            focusView = campos[6];
            cancel = true;
        }
        return cancel;
    }

}
