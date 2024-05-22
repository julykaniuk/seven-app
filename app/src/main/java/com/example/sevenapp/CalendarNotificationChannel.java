package com.example.sevenapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class CalendarNotificationChannel extends Application {
    public static final String CHANNEL_ID = "channel1";
    String channelDescription = getString(R.string.channel_description);

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_ID,
                    "Event notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription(channelDescription);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}