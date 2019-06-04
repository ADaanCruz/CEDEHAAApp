package com.adancruz.cedehaaapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;

public class ResetPasswordActivity extends AppCompatActivity {

    LinearLayout mResetPasswordView;
    private EditText correo, codigo, nueva_contrasena, conf_contrasena;
    private Button resetPassword, resendCodigo;
    private TextView instrucciones_reenviar;
    private LinearLayout area_codigos, area_contrasenas;

    private SecureRandom random;
    private String numberRandom;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mResetPasswordView = findViewById(R.id.vista_reset_password);
        correo = findViewById(R.id.rp_email);
        area_codigos = findViewById(R.id.rp_codigos);
        codigo = findViewById(R.id.rp_codigo);
        area_contrasenas = findViewById(R.id.rp_contrasenas);
        nueva_contrasena = findViewById(R.id.rp_new_password);
        conf_contrasena = findViewById(R.id.rp_verify_password);
        instrucciones_reenviar = findViewById(R.id.instrucciones_reenviar);
        resetPassword = findViewById(R.id.boton_reestablecer_contrasena);
        resendCodigo = findViewById(R.id.boton_reenviar_código);

        paso1();
        random = new SecureRandom();
        //cargarFondo();
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword.setEnabled(false);
                String texto_boton = resetPassword.getText().toString();
                if (texto_boton.equals(getResources().getText(R.string.mandar_codigo).toString())) {
                    random.nextBytes(new byte[1]);
                    numberRandom = Integer.toString(random.nextInt(9999)+1);
                    mandarCorreo("restaurar_contrasenia", "Reestablecer contraseña", resetPassword, ResetPasswordActivity.this);
                } else if (texto_boton.equals(getResources().getText(R.string.verificar_codigo).toString())) {
                    if (codigo.getText().toString().equals(numberRandom)) {
                        paso3();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this,
                                "El código es incorrecto",
                                Toast.LENGTH_LONG).show();
                        resetPassword.setEnabled(true);
                    }
                } else if (texto_boton.equals(getResources().getText(R.string.reestablecer_contrasena).toString())) {
                    Toast.makeText(ResetPasswordActivity.this,
                            "¡Contraseña reestablecida!",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        resendCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberRandom = Integer.toString(random.nextInt(9999)+1);
                mandarCorreo("restaurar_contrasenia", "Reestablecer contraseña", resetPassword, ResetPasswordActivity.this);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void cargarFondo() {
        Drawable drawable = getResources().getDrawable(R.drawable.fondo2_2);
        mResetPasswordView.setBackground(drawable);
    }

    private void paso1() {
        resetPassword.setEnabled(true);
        resetPassword.setText(getResources().getText(R.string.mandar_codigo));
        correo.setVisibility(View.VISIBLE);
        area_codigos.setVisibility(View.GONE);
        area_contrasenas.setVisibility(View.GONE);
        instrucciones_reenviar.setVisibility(View.GONE);
        resetPassword.setVisibility(View.VISIBLE);
        resendCodigo.setVisibility(View.GONE);
    }

    private void paso2() {
        resetPassword.setEnabled(true);
        resetPassword.setText(getResources().getText(R.string.verificar_codigo));
        correo.setVisibility(View.VISIBLE);
        area_codigos.setVisibility(View.VISIBLE);
        area_contrasenas.setVisibility(View.GONE);
        instrucciones_reenviar.setVisibility(View.VISIBLE);
        resetPassword.setVisibility(View.VISIBLE);
        resendCodigo.setVisibility(View.VISIBLE);
    }

    private void paso3() {
        resetPassword.setEnabled(true);
        resetPassword.setText(getResources().getText(R.string.reestablecer_contrasena));
        correo.setVisibility(View.VISIBLE);
        area_codigos.setVisibility(View.GONE);
        area_contrasenas.setVisibility(View.VISIBLE);
        instrucciones_reenviar.setVisibility(View.GONE);
        resetPassword.setVisibility(View.VISIBLE);
        resendCodigo.setVisibility(View.GONE);
    }

    private void mandarCorreo(String tipo, final String ventana, final Button boton, final Context context) {
        Toast.makeText(ResetPasswordActivity.this,
                "Enviando correo...",
                Toast.LENGTH_LONG).show();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Toast.makeText(ResetPasswordActivity.this,
                                "Correo enviado",
                                Toast.LENGTH_LONG).show();
                        paso2();
                    } else {
                        String error = jsonObject.getString("message");
                        String post = "post", send = "send", email = "email", tipo = "tipo";
                        if (error.equals(post)) {
                            Toast.makeText(context,
                                    "Error del servidor",
                                    Toast.LENGTH_LONG).show();
                        } else if (error.equals(email)) {
                            Toast.makeText(context,
                                    "El correo no está registrado",
                                    Toast.LENGTH_LONG).show();
                        } else if (error.equals(tipo)) {
                            Toast.makeText(context,
                                    "Error del servidor",
                                    Toast.LENGTH_LONG).show();
                        } else if (error.equals(send)) {
                            Toast.makeText(context,
                                    "No se pudo envíar el correo",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context,
                                    "Error del servidor",
                                    Toast.LENGTH_LONG).show();
                        }
                        boton.setEnabled(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,
                            "Error del servidor",
                            Toast.LENGTH_LONG).show();
                    boton.setEnabled(true);
                }
            }
        };

        EmailRequest emailRequest = new EmailRequest(
                correo.getText().toString(),
                tipo,
                numberRandom,
                responseListener
        );

        RequestQueue queue = Volley.newRequestQueue(ResetPasswordActivity.this);
        queue.add(emailRequest);
    }
}