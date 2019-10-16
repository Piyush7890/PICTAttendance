package com.piyush.pictattendance.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.piyush.pictattendance.R;

public class LaunchActivity extends BaseThemedActicity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },1000);
        startActivity(intent);
        overridePendingTransition(0,android.R.anim.fade_out);
        finish();
    }

    @Override
    protected int getDarkTheme() {
        return R.style.AppThemeDark;
    }

    @Override
    protected int getBlackTheme() {
        return getDarkTheme();
    }

    @Override
    protected int getStatusBarColor() {
        return Color.WHITE;
    }

    @Override
    protected int getLightTheme() {
        return R.style.AppTheme;
    }
}
