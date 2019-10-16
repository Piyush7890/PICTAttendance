package com.piyush.pictattendance.data.remote.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface AuthenticateApi {

    @POST("authenticate.do")
    Call<ResponseBody> authenticate(@Query("loginid") String loginId,
                                    @Query("password") String password,
                                    @Query("dbConnVar") String dbConnVar,
                                    @Query("service_id") String serviceId);

}
