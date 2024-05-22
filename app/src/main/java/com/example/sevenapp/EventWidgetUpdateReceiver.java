package com.example.sevenapp;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
public class EventWidgetUpdateReceiver extends BroadcastReceiver  {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("EventWidgetUpdate", "Broadcast received");
        if ("com.example.sevenapp.EVENT_DATABASE_UPDATED".equals(intent.getAction())) {
            Log.d("EventWidgetUpdate", "Updating widgets");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, EventWidget.class));
            EventWidget.updateAppWidgets(context, appWidgetManager, appWidgetIds);
        }
    }
}
