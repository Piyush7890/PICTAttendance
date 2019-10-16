package com.piyush.pictattendance.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import com.piyush.pictattendance.utils.Constants;
import com.piyush.pictattendance.utils.Utils;

@Entity(tableName = "Subjects")
public class Subject {

    @Override
    public boolean equals( Object obj) {
        if (!(obj instanceof Subject)) {
            return false;
        }
        Subject a = (Subject) obj;

        return subjectname.equals(a.subjectname);

    }

    @PrimaryKey(autoGenerate = true)
public int id;
    @ColumnInfo(name = "Attendance")
    private double attendance;

    @ColumnInfo(name = "LecturesPresent")
    private int present;

    @ColumnInfo(name = "SubjectName")
    private String subjectname;

    @ColumnInfo(name = "TotalLectures")
    private int total;

    @ColumnInfo(name = "Category")
    private  String category;

    @Ignore
    public boolean isSelected=false;

    @Ignore
    public boolean isChanged= false;


    @Ignore
    private Subject(double attendance, int present, String subjectname, int total, String category) {
        this.attendance = attendance;
        this.present = present;
        this.subjectname = subjectname;
        this.total = total;
        this.category = category;
    }

    public Subject(){}


    @Ignore
    public Subject(Subject subject)
    {
        attendance = subject.getAttendance();
        present = subject.getPresent();
        subjectname = subject.getSubjectname();
        total = subject.getTotal();
        category = subject.getCategory();
        isSelected = subject.isSelected;
        id = subject.id;
    }

    @Ignore
    public static Subject create(String subjectname, String total, String present)
    {

        if(Constants.DEBUG)
            Log.d("SUBJECTNAME",subjectname);

        int presentInt = Integer.parseInt(present);
        int totalInt = Integer.parseInt(total);
        double percentile = ((double) Math.round(100.0d * (((double) presentInt / ((double) totalInt)) * 100.0d))) / 100.0d;

        String[] splits = subjectname
                .split("-");

//        if(splits.length==3) {
//            String subjectName = Utils
//                    .toUpperCase(splits[1]
//                            .toLowerCase());
//            return new Subject(percentile,
//                    presentInt,
//                    subjectName,
//                    totalInt,
//                    Utils.category(splits[splits.length - 1].toLowerCase().replaceAll("\\s","")));
//        }
//        if(splits.length==2)
//        {
//            String subjectName = Utils
//                    .toUpperCase(splits[1]
//                            .toLowerCase());
//            return new Subject(percentile,
//                    presentInt,
//                    subjectName,
//                    totalInt,
//                   getCategoryByCheckingSubstring(subjectName));
//        }
//
//        if(splits.length==4)
//        {
//            String subjectName = Utils
//                    .toUpperCase(splits[1]
//                            .toLowerCase());
//            return new Subject(percentile, presentInt, subjectName+" "+splits[2],totalInt,Utils.category(splits[splits.length - 1].toLowerCase().replaceAll("\\s","")) );
//        }

        return new Subject(percentile,
                presentInt,
                Utils.toUpperCase(subjectname.replace("-"," ").toLowerCase()),
                totalInt,
                getCategoryByCheckingSubstring(subjectname));

        }


    public void reset(Subject temp)
    {
        setTotal(temp.getTotal());
        setPresent(temp.getPresent());
        setAttendance(temp.getAttendance());
        isChanged = false;
    }


    public void recalculate()
    {
         attendance = ((double) Math.round(100.0d * (((double) present / ((double) total)) * 100.0d))) / 100.0d;
            isChanged = true;
    }



    private static String getCategoryByCheckingSubstring(String subjectname)
    {
       String subjectName = subjectname.toLowerCase();
        if(subjectName.contains("lab")
                ||(subjectName.endsWith("pr")))
            return Utils.category("pr");

        if(subjectName.contains("workshop")
                ||( subjectName.endsWith("wr")))
            return Utils.category("wr");
        if(subjectName.contains("tutorial")
                ||(subjectName.endsWith("tut")))
            return Utils.category("tut");
        else return Utils.category("th");

    }

    public double getAttendance() {
        return attendance;
    }

    public void setAttendance(double attendance) {
        this.attendance = attendance;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
