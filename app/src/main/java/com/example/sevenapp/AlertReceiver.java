package com.example.sevenapp;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.sevenapp.CalendarNotificationChannel.CHANNEL_ID;
import static com.example.sevenapp.SettingsActivity.MyPreferences;
import static com.example.sevenapp.SettingsActivity.NotificationSound;

public class AlertReceiver extends BroadcastReceiver {
    public static final String NotificationEnabled = "NotificationEnabledKey";

    private SharedPreferences sharedPreferences;
    private String notificationSP;

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);

        String name = intent.getStringExtra("name");
        String startDate = intent.getStringExtra("start");
        String endDate = intent.getStringExtra("end");
        String startDate2 = "";
        int eid = intent.getIntExtra("eid", 0);
        int id = intent.getIntExtra("id", 0);

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startDate);
            startDate2 = new SimpleDateFormat("dd MMMM yyyy, HH:mm").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent activityIntent = new Intent(context, EventDetails.class);
        activityIntent.putExtra("NAME", name);
        activityIntent.putExtra("START_DATE", startDate);
        activityIntent.putExtra("END_DATE", endDate);

        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, eid, activityIntent, PendingIntent.FLAG_IMMUTABLE);


        // Перевіряємо дозвіл на відправку повідомлень
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            boolean isNotificationEnabled = sharedPreferences.getBoolean(NotificationEnabled, false);
            if (isNotificationEnabled) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Event notifications", NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);
                }

                Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.notifications)
                        .setContentTitle(name)
                        .setContentText(startDate2)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .build();

                notificationManager.notify(eid, notification);

                ring(context);
            }
        }
    }

    public void ring(Context context) {
        boolean isNotificationEnabled = sharedPreferences.getBoolean(NotificationEnabled, false);
        if (isNotificationEnabled) {
            notificationSP = sharedPreferences.getString(NotificationSound,
                    RingtoneManager.getActualDefaultRingtoneUri(context,
                            RingtoneManager.TYPE_NOTIFICATION).toString());
            Ringtone r = RingtoneManager.getRingtone(context, Uri.parse(notificationSP));
            r.play();
        }
    }}
