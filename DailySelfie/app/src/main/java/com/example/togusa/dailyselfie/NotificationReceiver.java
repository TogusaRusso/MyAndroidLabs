package com.example.togusa.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;




public class NotificationReceiver extends BroadcastReceiver{
    private Intent selfieIntent;
    private PendingIntent contentIntent;


    @Override
    public void onReceive(Context context, Intent intent) {

        selfieIntent = new Intent(context, SelfiesList.class);

        contentIntent = PendingIntent.getActivity(context, 0,
                selfieIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setContentText("Let's shoot another selfie")
                .setContentTitle("Daily Selfie")
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notificationBuilder.build());
    }
}
