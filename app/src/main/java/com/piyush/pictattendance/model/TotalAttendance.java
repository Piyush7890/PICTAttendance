package com.piyush.pictattendance.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "TotalAttendance")
public class TotalAttendance {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private double totalAttendance;
    private String date;
    private String month;
    @Ignore
    private double min;
    @Ignore
    private double max;
    public TotalAttendance(double totalAttendance, String date, String month) {
        this.month = month;
        this.totalAttendance = totalAttendance;
        this.date = date;
    }

    public double getTotalAttendance() {
        return totalAttendance;
    }

    public void setTotalAttendance(float totalAttendance) {
        this.totalAttendance = totalAttendance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }
}
