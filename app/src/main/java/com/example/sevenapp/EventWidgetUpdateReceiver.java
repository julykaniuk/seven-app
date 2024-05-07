package com.example.sevenapp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.util.Log;
import android.appwidget.AppWidgetManager;
public class EventWidgetUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, EventWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

        for (int appWidgetId : appWidgetIds) {
            EventWidget.updateAppWidget(context, appWidgetManager, appWidgetId); // Логування
        }
    }
}
