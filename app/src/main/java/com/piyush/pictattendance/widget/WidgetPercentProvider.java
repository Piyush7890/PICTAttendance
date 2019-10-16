package com.piyush.pictattendance.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import com.piyush.pictattendance.PICTAttendance;
import com.piyush.pictattendance.R;
import com.piyush.pictattendance.data.local.dao.PercentDao;
import com.piyush.pictattendance.model.TotalAttendance;
import com.piyush.pictattendance.ui.activities.MainActivity;
import com.piyush.pictattendance.utils.ColorUtil;
import com.piyush.pictattendance.utils.Constants;

import javax.inject.Inject;

public class WidgetPercentProvider extends AppWidgetProvider {

    
    @Inject
    PercentDao percentDao;
    boolean isDark;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ((PICTAttendance) context.getApplicationContext()).getComponent().inject(this);
        isDark = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.DARKWIDGETS,false);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_percent);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.percentage, pendingIntent);
        views.setTextColor(R.id.percentage, ColorUtil.getTextColor(!isDark,context));
        if (preferences.getBoolean(Constants.ISLOGGEDIN, false)) {

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    TotalAttendance totalAttendance = percentDao.getLastRecord();
                    views.setTextViewText(R.id.percentage, String.valueOf(totalAttendance.getTotalAttendance()) + "%");
                    for (int id : appWidgetIds)
                        appWidgetManager.updateAppWidget(id, views);
                    return null;
                }
            }.execute();

        } else {
            views.setTextViewText(R.id.percentage, "00.00%");
            for (int id : appWidgetIds)
                appWidgetManager.updateAppWidget(id, views);

        }
    }


    
}

