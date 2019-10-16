package com.piyush.pictattendance.model;

public class Category {
    private int id;
    private String text;
    private int color;

    public Category(int id,String text, int color) {
        this.id = id;
        this.text = text;
        this.color = color;
        this.isChecked = false;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isChecked;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public int getId() {
        return id;
    }
}
