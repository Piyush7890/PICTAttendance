package com.piyush.pictattendance.data;

import android.arch.lifecycle.LiveData;

import com.piyush.pictattendance.model.TotalAttendance;
import com.piyush.pictattendance.model.UserWrapper;
import com.piyush.pictattendance.utils.Resource;

import java.util.List;

public interface Repository {
     LiveData<List<TotalAttendance>> loadTotal();
     void loadAttendance(String username, String password);
     void cancel();
     void loadFromNetworkAndSave(String username, String password);
     void deleteAll();
    LiveData<Resource<UserWrapper>> getSubjects();

}
