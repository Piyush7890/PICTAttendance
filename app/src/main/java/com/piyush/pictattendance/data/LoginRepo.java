package com.piyush.pictattendance.data;

import android.arch.lifecycle.LiveData;

import com.piyush.pictattendance.utils.Status;

public interface LoginRepo {
     LiveData<Status> provideStatus();
     void login(String username, String password);
}
