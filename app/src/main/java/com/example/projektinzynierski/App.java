package com.example.projektinzynierski;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }
//test
    private void createNotificationChannels() {
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

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }

}
