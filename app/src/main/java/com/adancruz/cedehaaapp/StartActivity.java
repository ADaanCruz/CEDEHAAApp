package com.adancruz.cedehaaapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StartActivity extends AppCompatActivity {

    public static final String MY_PREFS_FILENAME = "com.adancruz.cedehaaappp.User";
    private SharedPreferences prefs;

    private ConnectivityManager con;
    private NetworkInfo network;
    private AlertDialog.Builder dialog;
    private boolean internet;
    private boolean selectionDialog;


    boolean start = false;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 50;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.

            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };

    public boolean mVisible;
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */

    VideoView videoCedehaa;
    Button btnOmitir;

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        dialog = new AlertDialog.Builder(this);
        prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);

        mContentView = findViewById(R.id.contenido_start);
        videoCedehaa = findViewById(R.id.video_cedehaa_inicio);
        btnOmitir = findViewById(R.id.boton_omitir);
        hide();

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.cedehaaintro;
        Uri uri = Uri.parse(videoPath);
        videoCedehaa.setVideoURI(uri);

        internet = verifyInternet(this);
        if (internet) {
            btnOmitir.setVisibility(View.VISIBLE);
            btnOmitir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnOmitir.setVisibility(View.GONE);
                    btnOmitir.setEnabled(false);
                    if (!start) {
                        start = true;
                        verifyPreferences();
                    }
                }
            });

            videoCedehaa.start();
            videoCedehaa.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    btnOmitir.setVisibility(View.GONE);
                    btnOmitir.setEnabled(false);
                    if (!start) {
                        start = true;
                        verifyPreferences();
                    }
                }
            });
        } else {
            btnOmitir.setVisibility(View.GONE);
            selectionDialog = false;
            mostrarDialog(
                    getString(R.string.verify_internet_dialog_message),
                    getString(R.string.verify_internet_dialog_title),
                    "Entendido",
                    "Configuración",
                    this,
                    "wifi"
            );
        }
    }

    private void verifyPreferences() {
        boolean guardarSesion = prefs.getBoolean("sesion", false);

        if (guardarSesion) {
            loginWithBD();
        } else {
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
        }
    }

    private void loginWithBD() {
        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        startActivity(new Intent(StartActivity.this, StudentActivity.class));
                        finish();
                    } else {
                        String error = jsonObject.getString("message");
                        String post = "post";
                        if (error.equals(post)) {
                            Toast.makeText(StartActivity.this,
                                    "Error: Type POST",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(StartActivity.this,
                                    "Acceso fallido, verifica los campos",
                                    Toast.LENGTH_LONG).show();
                        }
                        startActivity(new Intent(StartActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    Toast.makeText(StartActivity.this,
                            "ERROR: "+e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };

        prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);
        String correo = prefs.getString("correo", "");
        String contrasena = prefs.getString("contrasena", "");

        LoginRequest loginRequest = new LoginRequest(correo, contrasena, responseListener);
        RequestQueue queue = Volley.newRequestQueue(StartActivity.this);
        queue.add(loginRequest);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
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
                if (metodo.equals("wifi")) {
                    if (context.getClass() == StartActivity.class) {
                        finish();
                    }
                }
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
                        if (context.getClass() == StartActivity.class) {
                            finish();
                        }
                    }
                }
            });
        }
        dialog.show();
    }
}