package com.piyush.pictattendance.ui.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.piyush.pictattendance.data.Repository;
import com.piyush.pictattendance.model.TotalAttendance;
import com.piyush.pictattendance.model.UserWrapper;
import com.piyush.pictattendance.utils.Resource;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {


    private Repository repository;

    @Inject
     MainViewModel(Repository repository) {
        this.repository = repository;
    }

    void loadAttendance(String username, String password)
    {
       repository.loadAttendance(username,password);
    }

    void deleteAll()
    {
        repository.deleteAll();
    }

    LiveData<Resource<UserWrapper>> getSubjects()
    {
        return repository.getSubjects();
    }


    LiveData<List<TotalAttendance>> loadTotal()
    {
        return repository.loadTotal();
    }



    void requestLatestAttendance(String username, String password)
    {
        repository.loadFromNetworkAndSave(username,password);
    }

    public void cancelCall() {
            repository.cancel();
     }
}
