package com.piyush.pictattendance.utils;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.piyush.pictattendance.widget.WidgetListProvider;
import com.piyush.pictattendance.widget.WidgetPercentProvider;

public  class WidgetUtils {

    public static  void updateWidgets(Context context)
    {
        Intent intent = new Intent(context, WidgetListProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context.getApplicationContext())
                .getAppWidgetIds(new ComponentName(context.getApplicationContext(), WidgetListProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);

        intent = new Intent(context, WidgetPercentProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
         ids = AppWidgetManager.getInstance(context.getApplicationContext())
                .getAppWidgetIds(new ComponentName(context.getApplicationContext(), WidgetPercentProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

}
