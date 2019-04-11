package com.adancruz.cedehaaapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
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
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
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

    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
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

        mContentView = findViewById(R.id.contenido_start);
        videoCedehaa = (VideoView) findViewById(R.id.video_cedehaa_inicio);
        btnOmitir = (Button) findViewById(R.id.boton_omitir);
        hide();

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.cedehaaintro;
        Uri uri = Uri.parse(videoPath);
        videoCedehaa.setVideoURI(uri);

        ConnectivityManager con = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = con.getActiveNetworkInfo();

        if (network != null && network.isConnected()) {
            btnOmitir.setVisibility(View.VISIBLE);
            btnOmitir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnOmitir.setVisibility(View.GONE);
                    verifyPreferences();
                }
            });

            videoCedehaa.start();
            videoCedehaa.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    btnOmitir.setVisibility(View.GONE);
                    verifyPreferences();
                }
            });
        } else {
            btnOmitir.setVisibility(View.GONE);

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Necesitas conexión a Internet (WiFi o datos móviles) para el uso de la app.")
                    .setTitle("Verifica tu Internet");

            dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            dialog.show();
        }
    }

    private void verifyPreferences() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);
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

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);
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
}