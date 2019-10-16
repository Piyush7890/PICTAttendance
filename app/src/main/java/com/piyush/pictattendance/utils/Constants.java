package com.piyush.pictattendance.utils;

import com.piyush.pictattendance.model.Subject;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final String baseUrl = "http://pict.ethdigitalcampus.com:80/DCWeb/";
    public static final String EMAIL = "mamidwar.piyush@gmail.com";
    public static final String GITHUB_URL = "https://github.com/Piyush7890";
    public static final String SOURCE_CODE = "https://github.com/Piyush7890/PICTAttendance";
    public static final String INSTA_URL = "http://instagram.com/_u/_im__piyush__";
    public static final String FACEBOOK_URL = "https://www.facebook.com/piyush.mamidwar.75";
    public static final String LINKEDIN_URL = "https://www.linkedin.com/in/piyush-m2/";
    public static final String INSTA_BROWSER_URL = "http://instagram.com/_im__piyush__";
    public static final String attendanceUrl="http://pict.ethdigitalcampus.com/DCWeb/form/jsp_sms/";
    public static final String dbConnVar="PICT";
    public static final String serviceId="";
    public static final int dashboard = 1;
    public static final String ERROR = "Server is down or the credentials are invalid";
    public static final String NO_CONNECTION="No internet connection";
    public static final String NAME="Name";
    public static final String USERNAME="Username";
    public static final String PASSWORD="Password";
    public static final String YEAR="Year";
    public static final String ISLOGGEDIN="IsLoggedIn";
    public static final String DARKWIDGETS="darkWidgets";
    public static final String BROSWER_URL="https://pict.ethdigitalcampus.com/PICT/";


    public static final boolean DEBUG =true ;




    public static List<Subject> getFirst()
    {
        List<Subject> list = new ArrayList<>();
        list.add(Subject.create("Afoaufaoauauo aof aofhaoua hfa auo fahfofa o-PR",String.valueOf(9),String.valueOf(5)));
        list.add(Subject.create("B-PR",String.valueOf(11),String.valueOf(6)));
        list.add(Subject.create("C-PR",String.valueOf(8),String.valueOf(7)));
        list.add(Subject.create("D-PR",String.valueOf(13),String.valueOf(8)));
        list.add(Subject.create("E-PR",String.valueOf(14),String.valueOf(9)));
        list.add(Subject.create("F-PR",String.valueOf(19),String.valueOf(10)));
        list.add(Subject.create("G-PR",String.valueOf(14),String.valueOf(9)));
        return list;
    }

   public static List<Subject> getSecond()
    {
        List<Subject> list = new ArrayList<>();
        list.add(Subject.create("Afoaufaoauauo aof aofhaoua hfa auo fahfofa o-PR",String.valueOf(10),String.valueOf(6)));
        list.add(Subject.create("B-PR",String.valueOf(12),String.valueOf(3)));
        list.add(Subject.create("C-PR",String.valueOf(9),String.valueOf(4)));
        list.add(Subject.create("D-PR",String.valueOf(14),String.valueOf(5)));
        list.add(Subject.create("E-PR",String.valueOf(15),String.valueOf(6)));
        list.add(Subject.create("F-PR",String.valueOf(20),String.valueOf(10)));
        list.add(Subject.create("G-PR",String.valueOf(15),String.valueOf(9)));
        list.add(Subject.create("H-PR",String.valueOf(15),String.valueOf(11)));
        list.add(Subject.create("I-PR",String.valueOf(15),String.valueOf(13)));
        return list;
    }
}
