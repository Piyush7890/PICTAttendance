package com.piyush.pictattendance.model;

public class Update {

    private String subjectName;
    private double prev;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public double getPrev() {
        return prev;
    }

    public void setPrev(double prev) {
        this.prev = prev;
    }

    public double getCurr() {
        return curr;
    }

    public void setCurr(double curr) {
        this.curr = curr;
    }

    private double curr;

    public Update(String subjectName, double prev, double curr) {
        this.subjectName = subjectName;
        this.prev = prev;
        this.curr = curr;
    }


}
