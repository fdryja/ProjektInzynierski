package com.example.projektinzynierski;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Date;
import java.util.Random;


public class AlertsOdrobaczanie extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotificationOdrobaczanie(intent);
        notificationHelper.getManager().notify(m, nb.build());
    }
}