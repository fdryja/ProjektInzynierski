package com.example.projektinzynierski;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;


public class NotificationHelper extends ContextWrapper {
    private ArrayList<String> dgs;
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    private NotificationManager mManager;
    NotificationManager manager;
    public NotificationHelper(Context base) {
        super(base);
        createNotificationChannels();

    }

    //    @TargetApi(Build.VERSION_CODES.O)
//    private void createChannel() {
//        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
//        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//
//
//
////        getManager().createNotificationChannel(channel);
//    }
    public void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Powiadomienia o karmieniu",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");
            channel1.setLockscreenVisibility(1);
            channel1.enableVibration(true);
            channel1.enableLights(true);


            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Powiadomienia o weterynarzu",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("This is Channel 2");
            channel2.setLockscreenVisibility(1);
            channel2.enableVibration(true);
            channel2.enableLights(true);

            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }

    public NotificationManager getManager() {
//        if (manager == null) {
//            mManager = (NotificationManager) getSystemService(NotificationManager.class);
//        }

        return manager;
    }

    public NotificationCompat.Builder getChannelNotification(Intent intent) {
//        AlarmFragment af = new AlarmFragment();
//        dgs = af.passName();
//        int globalPosition = af.getGlobalPosition();
        int eatingINT = Integer.parseInt(intent.getExtras().getString("eating"));
        int eating_countINT = Integer.parseInt(intent.getExtras().getString("eating_count"));
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                .setContentTitle("Nakarm psa o imieniu "+intent.getExtras().getString("name"))
                .setContentText("Musisz podać "+(eatingINT/eating_countINT)+" gramów karmy.")
                .setSmallIcon(R.drawable.ic_android);
    }
    public NotificationCompat.Builder getChannelNotificationWizyta(Intent intent) {
//        AlarmFragment af = new AlarmFragment();
//        dgs = af.passName();
//        int globalPosition = af.getGlobalPosition();
        int eatingINT = Integer.parseInt(intent.getExtras().getString("eating"));
        int eating_countINT = Integer.parseInt(intent.getExtras().getString("eating_count"));
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_2_ID)
                .setContentTitle("Nakarm psa o imieniu "+intent.getExtras().getString("name"))
                .setContentText("Musisz podać "+(eatingINT/eating_countINT)+" gramów karmy.")
                .setSmallIcon(R.drawable.ic_android);
    }
}