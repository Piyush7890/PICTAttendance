package com.piyush.pictattendance.ui.activities.LoginActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.piyush.pictattendance.data.LoginRepo;
import com.piyush.pictattendance.utils.Status;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel{

    private LoginRepo repository;
@Inject
     LoginViewModel(LoginRepo repository) {
        this.repository = repository;
    }

      LiveData<Status> loadStatus()
    {
        return repository.provideStatus();
    }

     void login(String username, String password)
    {
        if(isLoginValid(username,password)) {
       repository.login(username, password);
        }
    }

    private boolean isLoginValid(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }
}

