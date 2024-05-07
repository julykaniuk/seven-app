package com.example.sevenapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import android.database.Cursor;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import android.app.PendingIntent;
import android.database.Cursor;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.example.sevenapp.EventDBHelper;
import com.example.sevenapp.EventWidgetUpdateReceiver;

import com.example.sevenapp.MainActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**
 * Implementation of App Widget functionality.
 */
public class EventWidget extends AppWidgetProvider {

    private static String extractTime(String fullDateTime) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.US);

        try {
            Date date = inputFormat.parse(fullDateTime);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.event_widget);

        // Відображення поточної дати
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.US);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.US);

        Date today = new Date();

        views.setTextViewText(R.id.day_widget, dayFormat.format(today));
        views.setTextViewText(R.id.date_widget, dateFormat.format(today));
        views.setTextViewText(R.id.month_widget, monthFormat.format(today));

        EventDBHelper dbHelper = new EventDBHelper(context);
        Cursor cursor = dbHelper.getEventForToday();

        String eventName = "Подія відсутня";
        if (cursor != null && cursor.moveToFirst()) {
            eventName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
            String eventDescription = cursor.getString(cursor.getColumnIndexOrThrow("NOTE"));
            String eventStartTime = cursor.getString(cursor.getColumnIndexOrThrow("START_DATE"));

            views.setTextViewText(R.id.title_widget, eventName);
            views.setTextViewText(R.id.description_widget, eventDescription);
            views.setTextViewText(R.id.time_widget, extractTime(eventStartTime));
        } else {
            views.setTextViewText(R.id.title_widget, "Подія відсутня");
            views.setTextViewText(R.id.description_widget, "Немає запланованих подій");
            views.setTextViewText(R.id.time_widget, "");
        }
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        views.setOnClickPendingIntent(R.id.event_widget, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);

        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}