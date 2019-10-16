package com.piyush.pictattendance.ui.activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.piyush.pictattendance.R;
import com.piyush.pictattendance.databinding.ActivitySettingBinding;
import com.piyush.pictattendance.service.NotificationJobService;
import com.piyush.pictattendance.utils.Constants;
import com.piyush.pictattendance.utils.Utils;
import com.piyush.pictattendance.utils.WidgetUtils;

public class SettingActivity extends BaseThemedActicity {
    ActivitySettingBinding binding;

    public static final int JOB_ID = 50;
    private static final long ONE_HR = 1000 * 60 * 60L;
    private static final long THREE_HRS = ONE_HR * 3L;
    private static final long SIX_HRS = ONE_HR * 6L;
    private static final long ONE_DAY = ONE_HR * 24L;
    public static final String PREF_TIME = "PREF_TIME";


    @Override
    protected int getDarkTheme() {
        return R.style.AppThemeDark_Settings;
    }

    @Override
    protected int getBlackTheme() {
        return R.style.AppThemeDark_Settings_Black;
    }

    @Override
    protected int getStatusBarColor() {
        return Color.parseColor("#f3f3f4");
    }

    @Override
    protected int getLightTheme() {
        return R.style.AppTheme_Login_Settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
       binding.setIsDark(isDark);
       binding.setIsWidgetDark(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.DARKWIDGETS,false));
        SharedPreferences.Editor editor = preferences.edit();
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.auto_fast);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.container.setOnClickListener(v -> {
            TransitionManager.beginDelayedTransition(binding.background, transition);
            binding.radioGroup.setVisibility(binding.radioGroup.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
        });
        RadioButton selectedButton = binding.never;
        switch (preferences.getInt(PREF_TIME, -1))
        {
            case -1:
                selectedButton = binding.never;
                break;
            case 3:
                selectedButton = binding.threeHr;
                break;
            case 6:
                selectedButton = binding.sixHr;
                break;
            case 24:
                selectedButton = binding.oneDay;
                break;

        }
        selectedButton.setChecked(true);
        binding.timeUpdateText.setText(selectedButton.getText());
        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton button = null;
            JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if(jobScheduler==null)
                return;
            jobScheduler.cancel(JOB_ID);
            ComponentName component = new ComponentName(this, NotificationJobService.class);
            JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, component);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            switch (checkedId)
            {
                case R.id.never:
                    editor.putInt(PREF_TIME, -1);
                    binding.timeUpdateText.setText(binding.never.getText());
                    return;
                case R.id.three_hr:
                    button = binding.threeHr;
                    editor.putInt(PREF_TIME, 3);
                    builder.setPeriodic(THREE_HRS);
                    break;
                case R.id.six_hr:
                    button = binding.sixHr;
                    editor.putInt(PREF_TIME, 6);
                    builder.setPeriodic(SIX_HRS);
                    break;

                case R.id.one_day:
                    button = binding.oneDay;
                    editor.putInt(PREF_TIME, 24);
                    builder.setPeriodic(ONE_DAY);
                    break;
            }
            binding.timeUpdateText.setText(button.getText());
            jobScheduler.schedule(builder.build());
            editor.apply();

        });


        binding.darkWgSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

                editor.putBoolean(Constants.DARKWIDGETS,isChecked);
                editor.apply();
            WidgetUtils.updateWidgets(SettingActivity.this);

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    public  void rateOnPlayStore(View V)
    {
        Utils.rateOnPlayStore(this);
    }
}
