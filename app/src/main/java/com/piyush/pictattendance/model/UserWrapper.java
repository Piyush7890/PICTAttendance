package com.piyush.pictattendance.model;

import java.util.List;

public class UserWrapper {

    private String name;
    private String year;
    private List<Subject> subjectList;
    private double percent;



    private boolean hasChanged;

    public UserWrapper(String name, String year, List<Subject> subjectList, double percent, boolean hasChanged) {
        this.name = name;
        this.year = year;
        this.subjectList = subjectList;
        this.percent = percent;
        this.hasChanged = hasChanged;
    }
    public boolean getHasChanged() {
        return hasChanged;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
