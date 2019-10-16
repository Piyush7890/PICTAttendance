package com.piyush.pictattendance.model;

public class MenuItem {
    private int iconId;
    private String name;

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuItem(int iconId, String name) {
        this.iconId = iconId;
        this.name = name;
    }
}
