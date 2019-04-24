package com.adancruz.cedehaaapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.support.v4.app.NotificationCompat.CATEGORY_RECOMMENDATION;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "NOTIFICATION";
    public static String token = "nothing";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("Mi token: ",s);
        token = s;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        Log.d(TAG, "Mensage recibido de: "+from);

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notificación: " + remoteMessage.getNotification().getBody());

            mostrarNotificacion(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data: " + remoteMessage.getData());
        }
    }

    public void mandarNotificacion() {
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(token+"@com.adancruz.cedehaaapp")
                .setMessageId(Integer.toString(0))
                .addData("title", "Notificación")
                .addData("notification", "Mensaje")
                .build());
    }

    private void mostrarNotificacion(String title, String body) {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.drawable.ic_stat_cedehaa)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(1)
                .setSound(soundUri)
                .setGroup("CURSO_NUEVO")
                .setGroupSummary(true)
                .setCategory(CATEGORY_RECOMMENDATION)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }
}
