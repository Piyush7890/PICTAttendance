package com.piyush.pictattendance.injection;

import com.piyush.pictattendance.data.remote.ResponseConvertor;
import com.piyush.pictattendance.data.remote.api.AttendanceApi;
import com.piyush.pictattendance.data.remote.api.AuthenticateApi;
import com.piyush.pictattendance.utils.Constants;

import java.net.CookieManager;
import java.net.CookiePolicy;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Module
public class NetworkModule {




    @Provides
    @Singleton
    CookieManager provideCookieManager()
    {
        CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return manager;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(CookieManager cookieManager)
    {
        return new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(cookieManager)).build();
    }

    @Provides
    @Singleton
    AuthenticateApi provideAuthenticateApi(OkHttpClient client)
    {
        return new Retrofit
                .Builder()
                .baseUrl(Constants.baseUrl).client(client)
                .build()
                .create(AuthenticateApi.class);
    }

    @Provides
    @Singleton
    AttendanceApi provideAttendanceApi(OkHttpClient client)
    {

        return new Retrofit
                .Builder()
                .baseUrl(Constants.attendanceUrl)
                .client(client)
                .addConverterFactory(new ResponseConvertor.Factory())
                .build()
                .create(AttendanceApi.class);

    }
}
