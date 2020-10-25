package com.example.projektinzynierski;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;


public class NotificationHelper extends ContextWrapper {
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    private NotificationManager mManager;
    NotificationManager manager;
    public NotificationHelper(Context base) {
        super(base);
        createNotificationChannels();

    }
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
        return manager;
    }

    public NotificationCompat.Builder getChannelNotification(Intent intent) {
        int eatingINT = Integer.parseInt(intent.getExtras().getString("eating"));
        int eating_countINT = Integer.parseInt(intent.getExtras().getString("eating_count"));
        String text ="Musisz podać "+(eatingINT/eating_countINT)+" gramów karmy.";
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                .setContentTitle("Nakarm psa o imieniu "+intent.getExtras().getString("name"))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setSmallIcon(R.drawable.ic_baseline_restaurant_menu_24);
    }

    public NotificationCompat.Builder getChannelNotificationSzczepienie(Intent intent) {
        String text = "Proszę odwiedzić weterynarza";
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_2_ID)
                .setContentTitle("Dzisiaj upływa termin szczepienia dla psa "+ intent.getExtras().get("name") +"!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setSmallIcon(R.drawable.ic_baseline_colorize_24);
    }

    public NotificationCompat.Builder getChannelNotificationOdrobaczanie(Intent intent) {
        String text = "Proszę odwiedzić weterynarza";
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_2_ID)
                .setContentTitle("Dzisiaj upływa termin odrobaczania dla psa "+ intent.getExtras().get("name") +"!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setSmallIcon(R.drawable.ic_baseline_local_hospital_24);
    }

    public NotificationCompat.Builder getChannelNotificationCritical(Intent intent) {
        int percentage = Integer.parseInt(intent.getExtras().get("percentage").toString());
        float weight = Float.parseFloat(intent.getExtras().get("full").toString());
        String procent = "";
        if(percentage == 1){
            procent ="10%";
        }else if(percentage == 2){
            procent ="20%";
        }
        else if(percentage == 3){
            procent ="30%";
        }else if(percentage == 4){
            procent ="40%";
        }else if(percentage == 5){
            procent ="50%";
        }
        String text = "Czas zamówić nowe opakowanie. Masa ostatniego opakowania wynosiła "+weight+ " kg.";
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                .setContentTitle("Zosatło tylko "+ procent+ " karmy dla psa "+ intent.getExtras().get("name") +"!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setSmallIcon(R.drawable.ic_baseline_published_with_changes_24);
    }
}