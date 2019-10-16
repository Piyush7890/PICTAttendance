package com.piyush.pictattendance.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.piyush.pictattendance.model.TotalAttendance;

import java.util.List;

@Dao
public interface PercentDao {


    @Query("SELECT * FROM TotalAttendance ORDER BY id DESC LIMIT 12")
    LiveData<List<TotalAttendance>> loadPercentages();

    @Query("DELETE FROM TotalAttendance")
    void deleteAll();


    @Query("SELECT * FROM TotalAttendance ORDER BY id DESC LIMIT 1")
    TotalAttendance getLastRecord();


    @Insert
    void insert(TotalAttendance attendance);

    @Insert
    void insertAll(List<TotalAttendance> list);
}
