package com.adancruz.cedehaaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class YourCoursesDetailsActivity extends AppCompatActivity {

    private Curso item;
    private TextView titulo, descripcionBreve, descripcionGeneral, fechaInicio;
    private ImageView imagen;
    private Button button, eliminarCurso;
    private String tipoDeUsuario = "";

    public static final String MY_PREFS_FILENAME = "com.adancruz.cedehaaappp.User";

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
        eliminarCurso = (Button) findViewById(R.id.boton_eliminar_curso);

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
            eliminarCurso.setVisibility(View.VISIBLE);

            textoBoton = "Editar curso";
            button.setText(textoBoton);
        } else if (tipoDeUsuario.equals("estudiante")){
            eliminarCurso.setVisibility(View.GONE);

            if (item.getEstado().equals("Cerrado")) {
                textoBoton = "Curso cerrado";
            } else if (item.getEstado().equals("Abierto")) {
                textoBoton = "Inscribirse";
            } else {
                textoBoton = "Estudiante";
                button.setVisibility(View.GONE);
            }
            button.setText(textoBoton);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                String txtBoton = button.getText().toString();
                if (txtBoton.equals("Editar curso")) {
                    intent = new Intent(YourCoursesDetailsActivity.this, CreateCourseActivity.class);

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
                } else if (txtBoton.equals("Curso cerrado")) {
                    Toast.makeText(YourCoursesDetailsActivity.this,
                            "El grupo está cerrado", Toast.LENGTH_LONG).show();
                } else if (txtBoton.equals("Inscribirse")) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    Toast.makeText(YourCoursesDetailsActivity.this,
                                            "¡Solicitud enviada!", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    String error = jsonObject.getString("message");
                                    String exist = "exist", insert = "insert", post = "post";
                                    if (error.equals(exist)) {
                                        Toast.makeText(YourCoursesDetailsActivity.this,
                                                "Su solicitud sigue en espera",
                                                Toast.LENGTH_LONG).show();
                                    } else if (error.equals(insert)) {
                                        Toast.makeText(YourCoursesDetailsActivity.this,
                                                "Error: Type SQL ",
                                                Toast.LENGTH_LONG).show();
                                    } else if (error.equals(post)) {
                                        Toast.makeText(YourCoursesDetailsActivity.this,
                                                "Error: Type POST",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(YourCoursesDetailsActivity.this,
                                                "Algo salió mal",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                Toast.makeText(YourCoursesDetailsActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    };

                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);
                    SolicitCourseRequest solicitRequest = new SolicitCourseRequest(
                            item.getTitulo(),
                            item.getFechaInicio(false),
                            prefs.getString("nombre", null),
                            prefs.getString("apellidoPaterno", null),
                            prefs.getString("correo", null),
                            prefs.getString("telefono", null),
                            responseListener
                    );
                    RequestQueue queue = Volley.newRequestQueue(YourCoursesDetailsActivity.this);
                    queue.add(solicitRequest);
                }
            }
        });

        eliminarCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(YourCoursesDetailsActivity.this,
                                        "¡Curso eliminado!", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                String error = jsonObject.getString("message");
                                String delete = "delete", post = "post";
                                if (error.equals(delete)) {
                                    Toast.makeText(YourCoursesDetailsActivity.this,
                                            "Error: Type SQL",
                                            Toast.LENGTH_LONG).show();
                                } else if(error.equals(post)) {
                                    Toast.makeText(YourCoursesDetailsActivity.this,
                                            "Error: Type POST",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(YourCoursesDetailsActivity.this,
                                            "Algo salió mal",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            Toast.makeText(YourCoursesDetailsActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                };

                DeleteCourseRequest registerRequest = new DeleteCourseRequest(
                        item.getTitulo(),
                        item.getFechaInicio(false),
                        responseListener
                );
                RequestQueue queue = Volley.newRequestQueue(YourCoursesDetailsActivity.this);
                queue.add(registerRequest);
            }
        });

    }
}