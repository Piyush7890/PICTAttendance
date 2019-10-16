package com.piyush.pictattendance.ui.activities.LoginActivity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.TransitionRes;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.piyush.pictattendance.PICTAttendance;
import com.piyush.pictattendance.R;
import com.piyush.pictattendance.databinding.ActivityLoginBinding;
import com.piyush.pictattendance.ui.activities.BaseThemedActicity;
import com.piyush.pictattendance.utils.Constants;
import com.piyush.pictattendance.ui.activities.views.FadingSnackbar;
import com.piyush.pictattendance.utils.transition.MorphTransform;
import com.piyush.pictattendance.utils.Utils;


import javax.inject.Inject;

public class LoginActivity extends BaseThemedActicity {

    @Inject
    ViewModelProvider.Factory factory;
    LoginViewModel viewModel;
    ActivityLoginBinding loginBinding;
    private SparseArray<Transition> transitions = new SparseArray<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        MorphTransform.setup(this,
                loginBinding.container,
                isDark?(theme==1?getResources().getColor(R.color.dialogBackgroundDark):getResources().getColor(R.color.dialogBackgroundBlack)):Color.WHITE,
                Math.round(Utils.convertDpToPixel(8,this)));
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        ((PICTAttendance) getApplication()).getComponent().inject(this);
        String username = preferences.getString(Constants.USERNAME, null);
        if(username!=null)
        loginBinding.etLogin.setText(username);
        viewModel = ViewModelProviders.of(this,factory).get(LoginViewModel.class);
        viewModel.loadStatus().observe(this, status -> {
            switch (status)
            {
                case SUCCESS:
                    saveUserNameAndPassword();
                    break;
                case LOADING:
                    showProgress();
                    break;
                case ERROR:
                    loginBinding.snackbar.show( "Invalid credentials or server is down",
                            FadingSnackbar.LENGTH.LENGTH_SHORT,
                            false);
                    hideProgress();
                    break;
                case NO_CONNECTION:
                    loginBinding.snackbar.show( "No internet connection!",
                            FadingSnackbar.LENGTH.LENGTH_SHORT,
                            false);
                    hideProgress();
                    break;

            }
        });
        loginBinding.etPassword.setOnEditorActionListener((v, actionId, event) -> {
            viewModel.login(loginBinding.etLogin.getText().toString(),
                    loginBinding.etPassword.getText().toString());
            loginBinding.etPassword.clearFocus();
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
            return true;
        });
        loginBinding.btLogin.setOnClickListener(v ->
                viewModel.login(loginBinding.etLogin.getText().toString(),
        loginBinding.etPassword.getText().toString()));
        loginBinding.btnCancel.setOnClickListener(v-> onBackPressed());
        loginBinding.parent.setOnClickListener(v->{
if(loginBinding.progress.getVisibility()==View.GONE)

            onBackPressed();});
    }

    @Override
    protected int getDarkTheme() {
        return R.style.AppTheme_Login_Dark;
    }

    @Override
    protected int getBlackTheme() {return R.style.AppTheme_Login_Dark_Black;}

    @Override
    protected int getStatusBarColor() {
        return Color.WHITE;
    }

    @Override
    protected int getLightTheme() {
        return R.style.AppTheme_Login;
    }

    private void hideProgress() {
        TransitionManager.beginDelayedTransition(loginBinding.container, getTransition(R.transition.auto));
        loginBinding.containerInternal.setVisibility(View.VISIBLE);
        loginBinding.progress.setVisibility(View.GONE);
    }

    private void showProgress() {
        TransitionManager.beginDelayedTransition(loginBinding.container, getTransition(R.transition.auto));
        loginBinding.containerInternal.setVisibility(View.GONE);
        loginBinding.progress.setVisibility(View.VISIBLE);
    }

    private void saveUserNameAndPassword() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.USERNAME,loginBinding.etLogin.getText().toString());
        editor.putString(Constants.PASSWORD,loginBinding.etPassword.getText().toString());
        editor.putBoolean(Constants.ISLOGGEDIN,true);
        editor.apply();
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }

    Transition getTransition(@TransitionRes int transitionId) {
        Transition transition = transitions.get(transitionId);
        if (transition == null) {
            transition = TransitionInflater.from(this).inflateTransition(transitionId);
            transitions.put(transitionId, transition);
        }
        return transition;
    }
}
