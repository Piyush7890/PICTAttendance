package com.piyush.pictattendance.utils;

import android.text.format.DateFormat;

import com.piyush.pictattendance.model.TotalAttendance;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class PercentageHelper {


    private Calendar calendar;

    @Inject
    PercentageHelper()
    {
        calendar = Calendar.getInstance();

    }

    public static String getDayOfMonthSuffix(final int n) {
    if(n < 1 || n > 31 )
        return "";
    if (n >= 11 && n <= 13) {
        return "th";
    }
    switch (n % 10) {
        case 1:  return "st";
        case 2:  return "nd";
        case 3:  return "rd";
        default: return "th";
    }
}

    public TotalAttendance getTotalAttendanceObject(double percent)
    {

        Date date = calendar.getTime();
        String month = ((String) DateFormat.format("MMM", date));
        String day  = (String) DateFormat.format("dd",   date);
        day = day+getDayOfMonthSuffix(Integer.parseInt(day));
        return new TotalAttendance(percent,day,month);
    }
}
