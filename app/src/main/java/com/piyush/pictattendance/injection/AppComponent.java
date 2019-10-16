package com.piyush.pictattendance.injection;

import com.piyush.pictattendance.service.NotificationJobService;
import com.piyush.pictattendance.ui.activities.LoginActivity.LoginActivity;
import com.piyush.pictattendance.ui.activities.MainActivity;
import com.piyush.pictattendance.widget.RemoteViewsFactory;
import com.piyush.pictattendance.widget.WidgetListProvider;
import com.piyush.pictattendance.widget.WidgetPercentProvider;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class,
        ViewModelModule.class,
        DBModule.class,
        NetworkModule.class,
        RepoModule.class})
public interface AppComponent {

    void inject(MainActivity activity);
    void inject(LoginActivity loginActivity);
    void inject(NotificationJobService notificationJobService);
    void inject(RemoteViewsFactory remoteViewsFactory);
    void inject(WidgetListProvider widgetListProvider);
    void inject(WidgetPercentProvider widgetPercentProvider);
}
