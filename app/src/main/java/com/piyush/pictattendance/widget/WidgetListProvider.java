package com.piyush.pictattendance.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.piyush.pictattendance.PICTAttendance;
import com.piyush.pictattendance.R;
import com.piyush.pictattendance.data.local.dao.PercentDao;
import com.piyush.pictattendance.model.TotalAttendance;
import com.piyush.pictattendance.ui.activities.MainActivity;
import com.piyush.pictattendance.utils.ColorUtil;
import com.piyush.pictattendance.utils.Constants;

import javax.inject.Inject;

public class WidgetListProvider extends AppWidgetProvider {


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);


    }

    @Inject
     PercentDao percentDao;
    private int darkColor;
boolean isDark;
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ((PICTAttendance) context.getApplicationContext()).getComponent().inject(this);
        darkColor = context.getResources().getColor(R.color.windowBackgroundDark);
        boolean isLoggedIn = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.ISLOGGEDIN,false);
         isDark = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.DARKWIDGETS,false);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.parent, pendingIntent);

        if(isLoggedIn) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    updateWidgetLayout(context, views);
                    for (int widgetId : appWidgetIds)

                    {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.attendance_list);
                        appWidgetManager.updateAppWidget(widgetId, views);
                    }
                    return null;
                }
            }.execute();
        }
        else
        {
            views.setViewVisibility(R.id.attendance_list,View.GONE);
            views.setViewVisibility(R.id.error,View.VISIBLE);
            views.setViewVisibility(R.id.header,View.GONE);
            for(int id: appWidgetIds)
            {
                appWidgetManager.updateAppWidget(id, views);
            }
        }



    }


    private void updateWidgetLayout(Context context, RemoteViews views)
    {
        Log.d("WIDGET", "WIDGET");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String name = preferences.getString(Constants.NAME,null);
        if(name==null)
            return;

        views.setViewVisibility(R.id.attendance_list,View.VISIBLE);
        views.setViewVisibility(R.id.header,View.VISIBLE);
        views.setTextViewText(R.id.name,name);
        views.setViewVisibility(R.id.error,View.GONE);
        views.setTextColor(R.id.name, ColorUtil.getTextColor(isDark,context));
        TotalAttendance lastRecord = percentDao.getLastRecord();
        if(lastRecord==null)
            return;
        views.setTextViewText(R.id.total, String.valueOf(lastRecord.getTotalAttendance())+"%");
        views.setTextColor(R.id.total, ColorUtil.getTextColor(isDark,context));
        views.setInt(R.id.parent, "setBackgroundResource", isDark?R.drawable.widget_bg_dark:R.drawable.widget_bg);
        //views.setInt(R.id.background,"setBackgroundColor",isDark ? darkColor : Color.WHITE);
//        views.setInt(R.id.header,"setBackgroundColor",isDark ? darkColor : Color.WHITE);
        views.setInt(R.id.header,"setBackgroundResource",isDark ? R.drawable.rounded_top_corners_divider_bg_dark : R.drawable.rounded_top_corners_divider_bg);
        Intent intent = new Intent(context,WidgetService.class);
        views.setRemoteAdapter(R.id.attendance_list, intent);

    }
}
