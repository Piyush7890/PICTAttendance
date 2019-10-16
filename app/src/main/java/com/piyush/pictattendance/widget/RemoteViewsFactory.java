package com.piyush.pictattendance.widget;

import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.piyush.pictattendance.PICTAttendance;
import com.piyush.pictattendance.R;
import com.piyush.pictattendance.data.local.dao.AttendanceDao;
import com.piyush.pictattendance.model.Subject;
import com.piyush.pictattendance.utils.ColorUtil;
import com.piyush.pictattendance.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
private boolean isDark;
    @Inject
    AttendanceDao attendanceDao;
    private List<Subject> subjects;
    private int color;
    private int dividerColor;


    RemoteViewsFactory(Context context) {
        ((PICTAttendance) context.getApplicationContext())
                .getComponent()
                .inject(this);
        this.context = context;

         color = ColorUtil.getTextColor(isDark,context);
        dividerColor = isDark ?
                context.getResources().getColor(R.color.dividerColorDark)
                : context.getResources().getColor(R.color.dividerColorLight);


    }

    @Override
    public void onCreate() {


    }

    @Override
    public void onDataSetChanged() {
        isDark = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(Constants.DARKWIDGETS, false);
        color = ColorUtil.getTextColor(isDark,context);
        dividerColor = isDark ?
                context.getResources().getColor(R.color.dividerColorDark)
                : context.getResources().getColor(R.color.dividerColorLight);

        subjects = attendanceDao.loadSubjectsSync();
        if(subjects==null)
            subjects = new ArrayList<>();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(subjects!=null)
        return subjects.size();
        else return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Subject subject = subjects.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        views.setTextViewText(R.id.subject_name, subject.getSubjectname());
        views.setTextColor(R.id.subject_name, color);
        views.setInt(R.id.divider, "setBackgroundColor", dividerColor);
        views.setTextViewText(R.id.percentage, String.valueOf(subject.getAttendance())+"%");
        views.setTextColor(R.id.percentage, color);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return subjects.get(position).id;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
