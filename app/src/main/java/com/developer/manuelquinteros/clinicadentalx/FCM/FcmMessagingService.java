package com.developer.manuelquinteros.clinicadentalx.FCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.developer.manuelquinteros.clinicadentalx.MainActivity;
import com.developer.manuelquinteros.clinicadentalx.R;
import com.developer.manuelquinteros.clinicadentalx.model.Notifications;
import com.developer.manuelquinteros.clinicadentalx.prefs.DatabaseHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FcmMessagingService extends FirebaseMessagingService {
    List<Notifications> list = new ArrayList<>();
    private DatabaseHelper db;

    @Override
    public void onNewToken(String s) {
        Log.e("NEW_TOKEN", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        db = new DatabaseHelper(this);

        String tit = remoteMessage.getNotification().getTitle();
        String bod = remoteMessage.getNotification().getBody();
        String exp =  remoteMessage.getData().get("expiry_date");
        String dis = remoteMessage.getData().get("discount");

        if (remoteMessage.getNotification() != null) {
            displayNotification(remoteMessage.getNotification(), remoteMessage.getData());
            sendNewPromoBroadcast(remoteMessage);
            createNotification(tit, bod, exp, dis);
        }


    }

    public void sendNewPromoBroadcast(RemoteMessage remoteMessage) {
        Intent intent = new Intent(PromotionsActivity.ACTION_NOTIFY_NEW_PROMO);
        intent.putExtra("title", remoteMessage.getNotification().getTitle());
        intent.putExtra("description", remoteMessage.getNotification().getBody());
        intent.putExtra("expiry_date", remoteMessage.getData().get("expiry_date"));
        intent.putExtra("discount", remoteMessage.getData().get("discount"));
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(intent);
    }


    public void displayNotification(RemoteMessage.Notification notification, Map<String, String> data) {

        String CHANNEL_ID = getString(R.string.default_notification_channel_id);
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        int NOTIFICATION_ID = 234;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(mChannel);
            }

        }

        Intent resultIntent = new Intent(this, PromotionsActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Aprovecha los mejores servicios en nuestras clinicas. Tenemos las mejores ofertas para ti."))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(getResources().getColor(android.R.color.holo_red_dark))
                .addAction(R.drawable.ic_launcher_foreground, "BANDEJA DE ENTRADA", pendingIntent);
                //.addAction(R.drawable.ic_launcher_foreground, "More", resultPendingIntent)
                //.addAction(R.drawable.ic_launcher_foreground, "And more", resultPendingIntent);


        if (notificationManager != null) {

            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }


    }

    private void createNotification(String title, String description, String expiry, String discount) {

        long id = db.insertNotification(title, description, expiry, discount);

        Notifications notifications = db.getNotification(id);

        if (notifications != null) {

            list.add(0, notifications);
        }
    }
}
