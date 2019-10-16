package com.piyush.pictattendance.utils;



import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppExecutors {
    private Executor diskExecutor;

    @Inject
     AppExecutors() {
        diskExecutor = Executors.newSingleThreadExecutor();

    }
    public Executor diskIO()
    {
        return diskExecutor;
    }

}
