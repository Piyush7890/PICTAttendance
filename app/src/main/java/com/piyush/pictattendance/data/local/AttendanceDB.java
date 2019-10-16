package com.piyush.pictattendance.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.piyush.pictattendance.data.local.dao.AttendanceDao;
import com.piyush.pictattendance.data.local.dao.PercentDao;
import com.piyush.pictattendance.model.Subject;
import com.piyush.pictattendance.model.TotalAttendance;

@Database(entities = {Subject.class, TotalAttendance.class},
        version = 1,
        exportSchema = false)
public abstract class AttendanceDB extends RoomDatabase {
   public abstract AttendanceDao attendanceDao();

   public abstract PercentDao percentDao();
}
