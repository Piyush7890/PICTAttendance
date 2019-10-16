package com.piyush.pictattendance.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.piyush.pictattendance.PICTAttendance;
import com.piyush.pictattendance.utils.PercentageHelper;
import com.piyush.pictattendance.data.local.dao.AttendanceDao;
import com.piyush.pictattendance.data.local.dao.PercentDao;
import com.piyush.pictattendance.data.remote.api.AttendanceApi;
import com.piyush.pictattendance.data.remote.api.AuthenticateApi;
import com.piyush.pictattendance.model.TotalAttendance;
import com.piyush.pictattendance.model.UserWrapper;
import com.piyush.pictattendance.utils.AppExecutors;
import com.piyush.pictattendance.utils.Comparator;
import com.piyush.pictattendance.utils.Constants;
import com.piyush.pictattendance.utils.Status;
import java.util.Collections;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepoImpl implements LoginRepo{
    private Context application;
    private AuthenticateApi authenticateApi;
    private AttendanceApi attendanceApi;
    private MutableLiveData<Status> _status;
    private AppExecutors executors;
    private AttendanceDao attendanceDao;
    private PercentDao percentDao;
    private PercentageHelper percentHelper;

    @Inject
    LoginRepoImpl(Context application,
              AppExecutors executors,
              AttendanceDao attendanceDao,
              AuthenticateApi authenticateApi,
              AttendanceApi attendanceApi,
              PercentDao percentDao,
              PercentageHelper percentHelper
              )
    {
        this.percentHelper = percentHelper;
        this.executors = executors;
        this.attendanceDao = attendanceDao;
        this.percentDao = percentDao;
        _status = new MutableLiveData<>();
        this.application = application;
        this.authenticateApi = authenticateApi;
        this.attendanceApi = attendanceApi;
    }

    public LiveData<Status> provideStatus()
    {
        return _status;
    }

    public void login(String username, String password)
    {
        _status.setValue(Status.LOADING);

        authenticateApi.authenticate(username,
                password,
                Constants.dbConnVar,
                Constants.serviceId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {

                        attendanceApi.getAttendance(Constants.dashboard,
                                Constants.dbConnVar,
                                username,
                                password,
                                Constants.serviceId).enqueue(new Callback<UserWrapper>() {
                            @Override
                            public void onResponse(@NonNull Call<UserWrapper> call,
                                                   @NonNull Response<UserWrapper> response) {
                                if(response.body()==null)
                                    _status.setValue(Status.ERROR);
                                else {
                                    ((PICTAttendance) application).submitUser(response.body());
                                   executors.diskIO().execute(()->{
                                       Collections.sort(response.body().getSubjectList(), new Comparator());
                                       attendanceDao.insertAll(response.body().getSubjectList());
                                       TotalAttendance old = percentDao.getLastRecord();
                                       if(old==null || old.getTotalAttendance()!=response.body().getPercent())
                                            percentDao.insert(percentHelper
                                                    .getTotalAttendanceObject(response.body().getPercent()));
                                   });
                                    _status.setValue(Status.SUCCESS);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<UserWrapper> call,
                                                  @NonNull Throwable t) {
                                _status.setValue(Status.ERROR);
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {

                        _status.setValue(Status.NO_CONNECTION);
                    }
                });

    }
}
