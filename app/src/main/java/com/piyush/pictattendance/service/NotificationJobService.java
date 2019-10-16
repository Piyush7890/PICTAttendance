package com.piyush.pictattendance.service;

//import com.piyush.pictattendance.utils.Status;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.arch.lifecycle.MediatorLiveData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.piyush.pictattendance.PICTAttendance;
import com.piyush.pictattendance.R;
import com.piyush.pictattendance.data.Repository;
import com.piyush.pictattendance.data.local.dao.AttendanceDao;
import com.piyush.pictattendance.data.local.dao.PercentDao;
import com.piyush.pictattendance.data.remote.api.AttendanceApi;
import com.piyush.pictattendance.data.remote.api.AuthenticateApi;
import com.piyush.pictattendance.model.TotalAttendance;
import com.piyush.pictattendance.model.UserWrapper;
import com.piyush.pictattendance.ui.activities.MainActivity;
import com.piyush.pictattendance.utils.AppExecutors;
import com.piyush.pictattendance.utils.Comparator;
import com.piyush.pictattendance.utils.Constants;
import com.piyush.pictattendance.utils.PercentageHelper;
import com.piyush.pictattendance.utils.Resource;
//import com.piyush.pictattendance.utils.Status;

import java.util.Collections;
import javax.inject.Inject;
import dagger.Lazy;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationJobService extends JobService {



    private enum Status{LOADING, DISMISS, UPDATED}

    @Inject
    AuthenticateApi authenticateApi;

    @Inject
    AttendanceApi attendanceApi;

    @Inject
    Lazy<AttendanceDao> attendanceDao;

    @Inject
    Repository repository;

    @Inject
    PercentDao percentDao;

    @Inject
    AppExecutors executors;


    @Inject
    PercentageHelper percentHelper;

    private static final String channelId="18";
    public static final String CHANNEL_ID = "udpate-attendance";
    public static final int NOTIFICATION_ID = 20;
    public static final int REQUEST_CODE = 200;

    NotificationManager notificationManager;
    MediatorLiveData<Resource<UserWrapper>> subjects;

    private Call<UserWrapper> attendanceCall;

    private Call<ResponseBody> authenticationCall;

    @Override
    public void onCreate() {
        super.onCreate();
        ((PICTAttendance) getApplication()).getComponent().inject(this);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
Log.d("STARTED", "STARTED");
        notificationManager = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = preferences.getString(Constants.USERNAME,null);
        String password = preferences.getString(Constants.PASSWORD,null);


        if(username==null || password ==null || notificationManager==null)
            return false;



        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Update Attendance",
                    NotificationManager.IMPORTANCE_LOW);
                    notificationManager.createNotificationChannel(channel);
        }

        showNotification(Status.LOADING);
      authenticationCall = authenticateApi.authenticate(username,password, Constants.dbConnVar, Constants.serviceId);
                authenticationCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        attendanceCall = attendanceApi.getAttendance(Constants.dashboard,
                                Constants.dbConnVar,
                                username,
                                password,
                                Constants.serviceId);
                        attendanceCall.enqueue(new Callback<UserWrapper>() {
                            @Override
                            public void onResponse(Call<UserWrapper> call, Response<UserWrapper> response) {
                                if(response.body()==null)
                                {
                                    showNotification(Status.DISMISS);
                                    jobFinished(params,true);

                                }

                                else {
                                    executors.diskIO().execute(() -> {
                                        attendanceDao.get().deleteAll();
                                        Collections.sort(response.body().getSubjectList(), new Comparator());
                                        attendanceDao.get().insertAll(response.body().getSubjectList());
                                        TotalAttendance old = percentDao.getLastRecord();
                                        if(old==null || old.getTotalAttendance()!=response.body().getPercent()) {
                                            percentDao.insert(percentHelper.getTotalAttendanceObject(response.body().getPercent()));
                                            showNotificationSuccess(response.body().getPercent());
                                        }
                                        else {
                                            showNotification(Status.DISMISS);
                                        }
                                        jobFinished(params,false);
                                        });

                                }
                            }

                            @Override
                            public void onFailure(Call<UserWrapper> call, Throwable t) {
                                            showNotification(Status.DISMISS);
                                jobFinished(params,true);

                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showNotification(Status.DISMISS);
                        jobFinished(params,true   );


                    }
                });


        return true;
    }

    private void showNotificationSuccess(double percent)
    {
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(getApplicationContext(), CHANNEL_ID)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_scc)
                .setContentIntent(PendingIntent
                        .getActivity(getApplicationContext()
                                ,REQUEST_CODE
                                ,new Intent(this
                                        , MainActivity.class)
                                , PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("Attendance Updated")
                .setAutoCancel(true)
                .setContentText("Your attendance is " + String.valueOf(percent));
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void showNotification(Status status) {
        NotificationCompat.Builder builder;
        switch (status)
        {
            case LOADING: {
                builder = new NotificationCompat
                        .Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_scc)
                        .setOngoing(true)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentTitle("Updating Attendance");
                notificationManager.notify(NOTIFICATION_ID, builder.build());

                break;
            }

            case DISMISS:
                notificationManager.cancel(NOTIFICATION_ID);
                break;
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        if(authenticationCall!=null)
        authenticationCall.cancel();
        if(attendanceCall!=null)
        attendanceCall.cancel();
        return false;
    }
}
