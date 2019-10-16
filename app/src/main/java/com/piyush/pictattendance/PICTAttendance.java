package com.piyush.pictattendance;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.piyush.pictattendance.injection.AppComponent;
import com.piyush.pictattendance.injection.AppModule;
import com.piyush.pictattendance.injection.DaggerAppComponent;
import com.piyush.pictattendance.model.UserWrapper;

public class PICTAttendance extends Application{



    private AppComponent component;
    private UserWrapper subjectList;
    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();

    }

    public void submitUser(UserWrapper userWrapper)
    {
        this.subjectList =userWrapper ;
    }

    public UserWrapper getUser()
    {
        return subjectList;
    }


    public AppComponent getComponent() {
        return component;
    }
}
