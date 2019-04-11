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
    private Button verAlumnos, button, eliminarCurso;
    private String tipoDeUsuario = "";

    public static final String MY_PREFS_FILENAME = "com.adancruz.cedehaaappp.User";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_yc_details_list);

        item = (Curso) getIntent().getSerializableExtra("objectData");

        titulo = findViewById(R.id.texto_titulo_de_tu_curso_details);
        imagen = findViewById(R.id.imagen_de_tu_curso_details);
        descripcionBreve = findViewById(R.id.texto_desc_breve_del_curso_details);
        descripcionGeneral = findViewById(R.id.texto_desc_gral_del_curso_details);
        fechaInicio = findViewById(R.id.texto_fecha_inicio_yc_details);
        verAlumnos = findViewById(R.id.boton_ver_alumnos_admin);
        button = findViewById(R.id.boton_detalle_de_curso);
        eliminarCurso = findViewById(R.id.boton_eliminar_curso);

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
            verAlumnos.setVisibility(View.VISIBLE);
            eliminarCurso.setVisibility(View.VISIBLE);

            textoBoton = "Editar curso";
            button.setText(textoBoton);
        } else if (tipoDeUsuario.equals("estudiante")){
            verAlumnos.setVisibility(View.GONE);
            eliminarCurso.setVisibility(View.GONE);

            switch (item.getEstado()) {
                case "Cerrado":
                    textoBoton = "Curso cerrado";
                    break;
                case "Abierto":
                    textoBoton = "Curso cerrado";
                    break;
                default:
                    textoBoton = "Curso inactivo";
                    button.setVisibility(View.GONE);
                    break;
            }
            button.setText(textoBoton);
            verifySolicit();
        }

        verAlumnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                String txtBoton = button.getText().toString();
                switch (txtBoton) {
                    case "Editar curso":
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
                        break;
                    case "Curso cerrado":
                        Toast.makeText(YourCoursesDetailsActivity.this,
                                "El grupo está cerrado", Toast.LENGTH_LONG).show();
                        break;
                    case "Inscrito":
                        Toast.makeText(YourCoursesDetailsActivity.this,
                                "Ya estás inscrito", Toast.LENGTH_LONG).show();
                        break;
                    case "Inscribirse":
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
                                        String post = "post",
                                                cursoyusuario = "cursoyusuario",
                                                verificar = "verificar",
                                                exist = "exist",
                                                totalylimite = "totalylimite",
                                                lleno = "lleno",
                                                insert = "insert";
                                        if (error.equals(post)) {
                                            Toast.makeText(YourCoursesDetailsActivity.this,
                                                    "Error: Type POST", Toast.LENGTH_LONG).show();
                                        } else if (error.equals(cursoyusuario)) {
                                            Toast.makeText(YourCoursesDetailsActivity.this,
                                                    "Hay un problema con el usuario", Toast.LENGTH_LONG).show();
                                        } else if (error.equals(verificar)) {
                                            Toast.makeText(YourCoursesDetailsActivity.this,
                                                    "Hay un problema con la verificación", Toast.LENGTH_LONG).show();
                                        } else if (error.equals(exist)) {
                                            Toast.makeText(YourCoursesDetailsActivity.this,
                                                    "Su solicitud sigue en espera", Toast.LENGTH_LONG).show();
                                        } else if (error.equals(totalylimite)) {
                                            Toast.makeText(YourCoursesDetailsActivity.this,
                                                    "Hay un problema con el curso", Toast.LENGTH_LONG).show();
                                        } else if (error.equals(lleno)) {
                                            Toast.makeText(YourCoursesDetailsActivity.this,
                                                    "El curso se acaba de llenar", Toast.LENGTH_LONG).show();
                                        } else if (error.equals(insert)) {
                                            Toast.makeText(YourCoursesDetailsActivity.this,
                                                    "Error: Type SQL - Insert ", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(YourCoursesDetailsActivity.this,
                                                    "Algo salió mal", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(YourCoursesDetailsActivity.this,
                                            "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        };

                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);
                        SolicitCourseRequest solicitRequest = new SolicitCourseRequest(
                                item.getTitulo(),
                                item.getFechaInicio(false),
                                prefs.getString("correo", null),
                                "no",
                                responseListener
                        );
                        RequestQueue queue = Volley.newRequestQueue(YourCoursesDetailsActivity.this);
                        queue.add(solicitRequest);
                        break;
                    default:
                        Toast.makeText(YourCoursesDetailsActivity.this,
                                "Regrese y vuelva a seleccionar el curso", Toast.LENGTH_LONG).show();
                        break;
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
                                            "Error: Type SQL", Toast.LENGTH_LONG).show();
                                } else if(error.equals(post)) {
                                    Toast.makeText(YourCoursesDetailsActivity.this,
                                            "Error: Type POST", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(YourCoursesDetailsActivity.this,
                                            "Algo salió mal", Toast.LENGTH_LONG).show();
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

    private void verifySolicit() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        String inscrito = jsonObject.getString("message");
                        String si = "inscrito", no = "no inscrito";
                        if (inscrito.equals(si)) {
                            String textoBoton = "Inscrito";
                            button.setText(textoBoton);
                        } else if (inscrito.equals(no)){
                            String textoBoton = "Inscribirse";
                            button.setText(textoBoton);
                        }
                    } else {
                        String error = jsonObject.getString("message");
                        String cursoyusuario = "cursoyusuario", post = "post", verificar = "verificar";
                        if (error.equals(cursoyusuario)) {
                            Toast.makeText(YourCoursesDetailsActivity.this,
                                    "Hay un problema con el curso", Toast.LENGTH_LONG).show();
                        } else if (error.equals(verificar)) {
                            Toast.makeText(YourCoursesDetailsActivity.this,
                                    "Hay un problema con la verificación", Toast.LENGTH_LONG).show();
                        } else if (error.equals(post)) {
                            Toast.makeText(YourCoursesDetailsActivity.this,
                                    "Error: Type POST", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(YourCoursesDetailsActivity.this,
                                    "Algo salió mal", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(YourCoursesDetailsActivity.this,
                            "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);
        SolicitCourseRequest solicitRequest = new SolicitCourseRequest(
                item.getTitulo(),
                item.getFechaInicio(false),
                prefs.getString("correo", null),
                "si",
                responseListener
        );
        RequestQueue queue = Volley.newRequestQueue(YourCoursesDetailsActivity.this);
        queue.add(solicitRequest);
    }
}