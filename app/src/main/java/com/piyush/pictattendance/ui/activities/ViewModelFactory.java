package com.piyush.pictattendance.ui.activities;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.piyush.pictattendance.data.Repository;

import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;


public class ViewModelFactory implements ViewModelProvider.Factory{


    private Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;

    @Inject
    public ViewModelFactory(Map<Class<? extends ViewModel>,Provider<ViewModel>> creators)
    {
        this.creators = creators;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : creators.entrySet()) {
            if (modelClass.isAssignableFrom(entry.getValue().get().getClass()))
                return (T) entry.getValue().get();
        }
        return null;
    }
}
