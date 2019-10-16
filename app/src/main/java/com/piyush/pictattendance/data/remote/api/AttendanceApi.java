package com.piyush.pictattendance.data.remote.api;

import com.piyush.pictattendance.model.Subject;
import com.piyush.pictattendance.model.UserWrapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AttendanceApi {
    @GET("StudentsPersonalFolder_pict.jsp")
    Call<UserWrapper> getAttendance(@Query("dashboard") int dashboard,
                                    @Query("dbConnVar") String dbConnVar,
                                    @Query("loginid") String loginId,
                                    @Query("password") String password,
                                    @Query("service_id") String serviceId);

}
