package com.piyush.pictattendance.injection;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.piyush.pictattendance.ui.activities.LoginActivity.LoginViewModel;
import com.piyush.pictattendance.ui.activities.MainViewModel;
import com.piyush.pictattendance.ui.activities.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel provideMainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel provideLoginViewModel(LoginViewModel mainViewModel);


    @Binds
    abstract ViewModelProvider.Factory provideFactory(ViewModelFactory factory);

}
