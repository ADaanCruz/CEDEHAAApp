package com.adancruz.cedehaaapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import static android.support.v4.app.NotificationCompat.CATEGORY_RECOMMENDATION;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String MY_PREFS_FILENAME = "com.adancruz.cedehaaappp.User";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public static final String TAG = "NOTIFICATION";
    public static final String TAG_FB = "Firebase";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e(TAG_FB, "NewToken: "+token);

        prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);
        String correo = prefs.getString("correo", null);
        if (correo != null) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            String message = jsonObject.getString("message");
                            if (message.equals("correcto")) {
                                Log.e(TAG_FB, "Token correctly!");
                            } else if (message.equals("success")) {
                                Log.e(TAG_FB, "Set new token correctly!");
                            }
                        } else {
                            String error = jsonObject.getString("message");
                            String post = "post", correo = "correo", token = "token", update = "update";
                            if (error.equals(post)) {
                                Log.e(TAG_FB, "Error: Post.");
                            } else if (error.equals(correo)) {
                                Log.e(TAG_FB, "Error: Correo.");
                            } else if (error.equals(token)) {
                                Log.e(TAG_FB, "Error: Token.");
                            } else if (error.equals(update)) {
                                Log.e(TAG_FB, "Error: Update.");
                            } else {
                                Log.e(TAG_FB, "Algo salió mal.");
                            }
                        }
                    } catch (JSONException e) {
                        Log.e(TAG_FB, "Error: "+e.getMessage());
                        e.printStackTrace();
                    }
                }
            };
            NewTokenRequest newTokenRequest = new NewTokenRequest(correo, token, responseListener);
            RequestQueue queue = Volley.newRequestQueue(MyFirebaseMessagingService.this);
            queue.add(newTokenRequest);
        }
        editor = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE).edit();
        editor.putString("token", token);
        editor.apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        Log.d(TAG, "Mensage recibido de: "+from);

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notificación: " + remoteMessage.getNotification().getBody());

            prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);
            if (prefs.getBoolean("notificaciones",true)) {
                mostrarNotificacion(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data: " + remoteMessage.getData());
        }
    }

    private void mostrarNotificacion(String title, String body) {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.drawable.ic_stat_cedehaa)
                .setAutoCancel(true)
                .setPriority(1)
                .setSound(soundUri)
                .setGroup("CURSO_NUEVO")
                .setGroupSummary(true)
                .setCategory(CATEGORY_RECOMMENDATION)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .setContentIntent(pendingIntent);

        prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);
        String tipoDeUsuario = prefs.getString("tipoDeUsuario","estudiante");
        assert tipoDeUsuario != null;
        if (tipoDeUsuario.equals("estudiante")) {
            notificationBuilder
                    .setContentTitle(title)
                    .setContentText(body);
        } else if (tipoDeUsuario.equals("administrador")){
            notificationBuilder
                    .setContentTitle("Curso nuevo")
                    .setContentText("Un administrador a creado un curso");
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
