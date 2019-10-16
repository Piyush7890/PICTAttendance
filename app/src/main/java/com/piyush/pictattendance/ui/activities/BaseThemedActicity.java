package com.piyush.pictattendance.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.piyush.pictattendance.utils.Utils;

public abstract class BaseThemedActicity extends AppCompatActivity {

    protected SharedPreferences preferences;
    protected int theme;
    protected boolean isDark;

    @Override
    public void setTheme(int resid) {
        preferences =  PreferenceManager
                .getDefaultSharedPreferences(this);
        theme =   preferences.getInt("theme",1);
        isDark = theme == 1 || theme == 2;
        if(theme == 1)
            super.setTheme(getDarkTheme());
        else if(theme==2)
            super.setTheme(getBlackTheme());
        else super.setTheme(getLightTheme());
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isDark)
        Utils.setLightStatusBar(this, getStatusBarColor());
    }

    protected abstract int getDarkTheme();
    protected  abstract int getBlackTheme();
    protected abstract int getStatusBarColor();
    protected abstract int getLightTheme();
}
