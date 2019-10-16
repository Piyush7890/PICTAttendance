package com.piyush.pictattendance.injection;

import android.app.Application;

import android.content.Context;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import com.piyush.pictattendance.R;
import com.piyush.pictattendance.data.LoginRepo;
import com.piyush.pictattendance.data.LoginRepoImpl;
import com.piyush.pictattendance.data.Repository;
import com.piyush.pictattendance.data.RepositoryImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application context;

    public AppModule(Application context) {
        this.context = context;
    }



    @Provides
    @Singleton
    Context provideContext()
    {
        return context;
    }


    @Provides
    @Singleton
    CustomTabsIntent provideCustomTabsIntentBuilder(Context context)
    {
        return new CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .addDefaultShareMenuItem()
                .setStartAnimations(context,R.anim.slide_up,R.anim.fade_out_slide_out)
                .setExitAnimations(context,R.anim.fade_in_slide_in,R.anim.slide_down).build();
    }


}
