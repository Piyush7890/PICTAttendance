package com.piyush.pictattendance.utils;

import com.piyush.pictattendance.model.Subject;

public class Comparator implements java.util.Comparator<Subject> {
    @Override
    public int compare(Subject o1, Subject o2) {
        return o1.getSubjectname().compareTo(o2.getSubjectname());
    }
}
