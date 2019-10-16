package com.piyush.pictattendance.ui.activities;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.job.JobScheduler;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.piyush.pictattendance.UpdateDialog;
import com.piyush.pictattendance.adapters.CategoryAdapter;
import com.piyush.pictattendance.adapters.GraphAdapter;
import com.piyush.pictattendance.PICTAttendance;
import com.piyush.pictattendance.R;
import com.piyush.pictattendance.adapters.SubjectsAdapter;
import com.piyush.pictattendance.data.local.dao.PercentDao;
import com.piyush.pictattendance.databinding.ActivityMainBinding;
import com.piyush.pictattendance.model.Subject;
import com.piyush.pictattendance.model.TotalAttendance;
import com.piyush.pictattendance.model.Update;
import com.piyush.pictattendance.model.UserWrapper;
import com.piyush.pictattendance.ui.activities.LoginActivity.LoginActivity;
import com.piyush.pictattendance.ui.activities.views.Chip;
import com.piyush.pictattendance.ui.activities.views.LoadingView;
import com.piyush.pictattendance.utils.AppExecutors;
import com.piyush.pictattendance.utils.Constants;
import com.piyush.pictattendance.ui.activities.views.FadingSnackbar;
import com.piyush.pictattendance.utils.ItemAnimators.SlideUpAlphaAnimator;
import com.piyush.pictattendance.utils.PercentageHelper;
import com.piyush.pictattendance.utils.WidgetUtils;
import com.piyush.pictattendance.utils.transition.MorphTransform;
import com.piyush.pictattendance.utils.Utils;
import com.piyush.pictattendance.utils.customtabs.CustomTabActivityHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

import dagger.Lazy;
import okhttp3.internal.Util;

@SuppressLint("RestrictedApi")
public class MainActivity extends BaseThemedActicity implements
        CategoryAdapter.Listener,
        SubjectsAdapter.Listener,
        FadingSnackbar.Listener {


    @Inject
    Lazy<CustomTabsIntent> customTabsIntent;
    private static final int LOGIN = 200;
String[] themes = {"Light", "Dark", "Lights Off"};
    CategoryAdapter categoryAdapter;
    @Inject
    ViewModelProvider.Factory factory;
    MainViewModel viewModel;
    SubjectsAdapter adapter;
    Boolean isLoggedIn;
    ActivityMainBinding binding;

    AlphaAnimation alphaAnimation;
    private List<Subject> list;

    GraphAdapter graphAdapter;
    private Transition auto_fast;
    private float finalElevation;
    LoadingView loadingView;
    private ActionBarDrawerToggle toggle;


    @Override
    public void onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(Gravity.START))
            binding.drawerLayout.closeDrawer(Gravity.START);
        else
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
         binding.github.setOnClickListener(v -> {CustomTabActivityHelper.openCustomTab(
                 MainActivity.this, customTabsIntent.get(),
                 Uri.parse(Constants.GITHUB_URL));
         closeDrawer();
         });
         binding.instagram.setOnClickListener(v -> {
             Uri uri = Uri.parse(Constants.INSTA_URL);
             Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

             likeIng.setPackage("com.instagram.android");
                closeDrawer();
             try {
                 startActivity(likeIng);
             } catch (ActivityNotFoundException e) {
                 CustomTabActivityHelper.openCustomTab(
                         MainActivity.this, customTabsIntent.get(),
                         Uri.parse(Constants.INSTA_BROWSER_URL));
             }
         });
         binding.mail.setOnClickListener(v -> {
                closeDrawer();
             startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.LINKEDIN_URL)));
         });
        binding.drawerLayout.requestDisallowInterceptTouchEvent(true);

        binding.navView.setNavigationItemSelectedListener(menuItem -> {
             switch (menuItem.getItemId()) {
                 case R.id.rate:
                     Utils.rateOnPlayStore(this);
                     return true;
                 case R.id.log_out:
                     binding.drawerLayout.closeDrawer(Gravity.START);
                     viewModel.cancelCall();
                     viewModel.deleteAll();
                     adapter.submitList(new ArrayList<>());
                     isLoggedIn = false;
                     new Handler().postDelayed(() -> {
                         TransitionManager.beginDelayedTransition(binding.parent);
                         invalidateOptionsMenu();
                         binding.categoryList.setVisibility(View.GONE);
                         binding.btnLogin.setVisibility(View.VISIBLE);
                         binding.swipeRefreshLayout.setEnabled(false);
                         binding.emptyItemViewstub.setVisibility(View.VISIBLE);
                         binding.headerLayout.header.setVisibility(View.GONE);
                     }, 50);
                     JobScheduler scheduler = ((JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE));
                     if(scheduler!=null)
                         scheduler.cancel(SettingActivity.JOB_ID);
                     SharedPreferences.Editor editor = PreferenceManager
                             .getDefaultSharedPreferences(MainActivity.this).edit();
                     editor.remove(Constants.PASSWORD);
                     editor.remove(Constants.NAME);
                     editor.remove(Constants.ISLOGGEDIN);
                     editor.apply();
                     WidgetUtils.updateWidgets(MainActivity.this);
                     list=null;
                     return true;

                 case R.id.open_in_browser:
                     CustomTabActivityHelper.openCustomTab(
                             MainActivity.this, customTabsIntent.get(),
                             Uri.parse(Constants.BROSWER_URL));
                     binding.drawerLayout.closeDrawer(Gravity.START);
                     return true;

                 case R.id.view_graph:
                     binding.drawerLayout.closeDrawer(Gravity.START);
                     toggleGraph();
                     return true;

                 case R.id.send_feedback:
                                  Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                 "mailto",Constants.EMAIL, null));
         emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding PICT Attendance");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.EMAIL});
         startActivity(Intent.createChooser(emailIntent, "Send email..."));
         binding.drawerLayout.closeDrawer(Gravity.START);
                     return true;


//                 case R.id.dark_theme:
//                     preferences
//                             .edit()
//                             .putBoolean("isDark", !isDark)
//                             .apply();
//                     restartApp();
//                     return true;
                 case R.id.settings:
                     startActivity(new Intent(MainActivity.this, SettingActivity.class),
                             ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                     return true;
             }
             return false;
         });

         graphAdapter = new GraphAdapter(binding.headerLayout.graph);
        isLoggedIn = preferences.getBoolean(Constants.ISLOGGEDIN, false);
        binding.setIsLoggedIn(isLoggedIn);
         alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setDuration(200L);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        binding.snackbar.setListener(this);
        setSupportActionBar(binding.toolbar);
//        CheckBox theme = ((CheckBox) binding.toolbar.getMenu().findItem(R.id.menu_theme).getActionView());
//        theme.setButtonDrawable(R.drawable.asl_theme);
//        theme.setChecked(isDark);
//        theme.jumpDrawablesToCurrentState();
        toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar, R.string.open, R.string.close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.categoryList.setLayoutManager(manager);
        categoryAdapter = new CategoryAdapter(this);
        categoryAdapter.setListener(this);
        binding.categoryList.setAdapter(categoryAdapter);
         finalElevation = Utils.convertDpToPixel(4, this);


        binding.appBarLayout.addOnOffsetChangedListener((appBarLayout, i) -> {
            if (Math.abs(i) == appBarLayout.getTotalScrollRange()) {
                binding.headerLayout.headerInner.animate().translationZ(finalElevation).setDuration(200).start();
            }
            else if (binding.headerLayout.headerInner.getTranslationZ() != 0) {
                binding.headerLayout.headerInner.animate().translationZ(0).setDuration(150).start();

            }
        });

        ((PICTAttendance) getApplication()).getComponent().inject(this);
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        inflateGraphLayout();

        binding.headerLayout.headerInner.setOnClickListener(v -> toggleGraph());
        adapter = new SubjectsAdapter(this, this);

        binding.btnLogin.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this,
                    LoginActivity.class);
            MorphTransform.addExtras(intent,
                    getResources().getColor(isDark ? R.color.colorAccentDark : R.color.colorAccent),
                    binding.btnLogin.getHeight() / 2);
            startActivityForResult(intent,
                    LOGIN,
                    ActivityOptions.makeSceneTransitionAnimation(this,
                            binding.btnLogin,
                            "transition")
                            .toBundle());

        });
        binding.subjectsList.setAdapter(adapter);
        binding.subjectsList.setItemAnimator(SlideUpAlphaAnimator.create());

        if (isLoggedIn) {


                    String username = preferences.getString(Constants.USERNAME, null);
            String password = preferences.getString(Constants.PASSWORD, null);
            viewModel.loadAttendance(username, password);
            viewModel.loadTotal().observe(this, list -> {
                if(list!=null) {
                    Collections.reverse(list);
                    graphAdapter.setData(list);
                    binding.headerLayout.graph.smoothScrollToPosition(list.size());

                }
            });

            viewModel.getSubjects().observe(this, listResource -> {

                binding.swipeRefreshLayout.setRefreshing(false);
//                binding.loadingView.setVisibility(View.GONE);
                binding.snackbar.dismiss();

                switch (listResource.status) {

                    case LOADED_FROM_DB:
                        if(listResource.data.getSubjectList()!=null) {
                            list = createList(listResource.data.getSubjectList());
                            calculateTotal(list, false, false);
                            setHeader(preferences.getString(Constants.NAME, null)
                                    , preferences.getString(Constants.YEAR, null));
                            adapter.submitList(listResource.data.getSubjectList());

                        }
                        break;
                    case SUCCESS: {
                        if(list!=null) {
                            List<Subject> newList = createList(listResource.data.getSubjectList());
                            List<Update> updates = new ArrayList<>();
                            for (Subject subject : newList) {
                                int index = list.indexOf(subject);
                                if (index != -1) {
                                    if (subject.getAttendance() != list.get(index).getAttendance()) {
                                        updates.add(new Update(subject.getSubjectname(),  list.get(index).getAttendance(),subject.getAttendance()));
                                    }
                                }
                                else
                                {
                                    updates.add(new Update(subject.getSubjectname(), 0,subject.getAttendance()));
                                }
                            }
                            if (!updates.isEmpty()) {
                                UpdateDialog dialog = new UpdateDialog();
                                dialog.setTheme(theme);
                                dialog.setList(updates);
                                dialog.show(getSupportFragmentManager(), "UPDATE");
                            }
                        }
                        list = createList(listResource.data.getSubjectList());
                        calculateTotal(list, false, false);
                        adapter.submitList(listResource.data.getSubjectList());

                        storePref(Constants.NAME, listResource.data.getName());
                        String year = listResource.data.getYear().replace(" ", " • ");
                        storePref(Constants.YEAR, year);
                        setHeader(listResource.data.getName(), year);
                        WidgetUtils.updateWidgets(this);
                        break;
                    }
                    case LOADING:
                        break;
                    case ERROR:
                        break;
                }
//                binding.subjectsList.setVisibility(View.VISIBLE);

            });
            binding.swipeRefreshLayout.setOnRefreshListener(() -> {
                binding.snackbar.show("Updating Attendance",
                        FadingSnackbar.LENGTH.LENGTH_SHORT,
                        true);
//                categoryAdapter.unSelectAll();
//                for(int i=0;i<manager.getItemCount();i++)
//                    ((Chip) manager.getChildAt(i)).animateCheckAndInvoke(false,null);
                viewModel.requestLatestAttendance(username, password);

//                TransitionManager.beginDelayedTransition(binding.parent);
//                binding.subjectsList.setVisibility(View.GONE);
//                binding.loadingView.setVisibility(View.VISIBLE);

            });
        }
    }


    private void closeDrawer()
    {
        binding.drawerLayout.closeDrawer(Gravity.START);
    }

    @Override
    protected int getDarkTheme() {
        return R.style.AppThemeDark;
    }

    @Override
    protected int getBlackTheme() {
        return R.style.AppThemeDark_Black;
    }

    @Override
    protected int getStatusBarColor() {
        return Color.WHITE;
    }

    @Override
    protected int getLightTheme() {
        return R.style.AppTheme;
    }

    private void inflateGraphLayout() {

        binding.headerLayout.graph.setAdapter(graphAdapter);

    }

    public void toggleGraph()
    {
        if(auto_fast==null)
        auto_fast = TransitionInflater.from(this).inflateTransition(R.transition.auto_fast);
            if(binding.headerLayout.graphParent.getVisibility()==View.VISIBLE) {

                    auto_fast.setDuration(175L);
                TransitionManager.beginDelayedTransition(binding.headerLayout.header, auto_fast);

                binding.headerLayout.studentName.setMaxLines(1);
                binding.headerLayout.graphParent.setVisibility(View.GONE);
            }
            else {
                auto_fast.setDuration(225L);

                TransitionManager.beginDelayedTransition(binding.headerLayout.header, auto_fast);
                binding.headerLayout.studentName.setMaxLines(Integer.MAX_VALUE);

                binding.headerLayout.graphParent.setVisibility(
                        View.VISIBLE);
            }

    }

    private List<Subject> createList(List<Subject> oldList) {
        List<Subject> newList = new ArrayList<>();
        if(oldList!=null){

            for (Subject subject : oldList)
            newList.add(new Subject(subject));}
        return newList;

    }


    private void restartApp() {
        Intent intent = new Intent(this, EmptyActivity.class);
        startActivity(intent);
        overridePendingTransition(0, android.R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dark_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu1) {
        Menu menu = binding.navView.getMenu();
        MenuItem logOut = menu.findItem(R.id.log_out);
        MenuItem settings = menu.findItem(R.id.settings);
        MenuItem graph = menu.findItem(R.id.view_graph);
      //  MenuItem dark = menu.findItem(R.id.dark_theme);
       // dark.setIcon(isDark ? R.drawable.ic_round_brightness_2_24px : R.drawable.ic_outline_round);
        logOut.setVisible(isLoggedIn);
        graph.setVisible(isLoggedIn);
        settings.setVisible(isLoggedIn);
//        CheckBox theme = ((CheckBox) menu1.findItem(R.id.change_theme).getActionView());
//        theme.setPaddingRelative(theme.getPaddingStart(),theme.getPaddingTop(),Math.round(Utils.convertDpToPixel(12,this)),theme.getPaddingBottom());
//        theme.setButtonDrawable(R.drawable.asl_theme);
//        theme.setChecked(isDark);
//        theme.jumpDrawablesToCurrentState();
//        theme.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            buttonView.postDelayed(() -> {
//                preferences
//                        .edit()
//                        .putBoolean("isDark", !isDark)
//                        .apply();
//                restartApp();
//            }, 800L);
//
//        });
                MenuItem darkIcon = menu1.findItem(R.id.change_theme);

         darkIcon.setIcon(isDark ? R.drawable.ic_theme_night : R.drawable.ic_theme_day);


        return true;

    }

    private class AnimationListener extends com.piyush.pictattendance.utils.AnimationListener
    {

        AnimationListener(String text) {
            this.text = text;
        }

        String text;


        @Override
        public void onAnimationRepeat(Animation animation) {
            binding.headerLayout.totalPercent.setText(text, false);
        }
    }

    private void calculateTotal(List<Subject> list, boolean animate, boolean fade) {

        double totalperc = 0f;
        int count =0 ;
        for (Subject subject : list) {
            if(subject.getTotal()!=0)
            {
                count++;
                totalperc += subject.getAttendance();
        }
        }
        Log.d("COUNT", String.valueOf(count));
        int total = count==0?list.size():count;
        final String totalPercText = list.isEmpty() ? "0%" : String.format("%s%%",
                String.valueOf(Utils.round(totalperc /total, 2)));
        if (fade) {

            alphaAnimation.setAnimationListener(new AnimationListener(totalPercText));
            binding.headerLayout.totalPercent.startAnimation(alphaAnimation);
            return;
        }


        binding.headerLayout.totalPercent.setText(totalPercText, animate);
    }

    private void setHeader(String name, String year) {
        if (name != null && year != null) {
            this.binding.headerLayout.studentYear.setText(year);
            this.binding.headerLayout.studentName.setText(name);

        }
    }

    private void storePref(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

if(item.getItemId()==R.id.change_theme)
{
    new AlertDialog.Builder(this)
            .setTitle("Change Theme")
            .setSingleChoiceItems(themes, theme, null)
            .setNegativeButton("CANCEL", null)
            .setPositiveButton("SET", (dialog, whichButton) -> {
                dialog.dismiss();
                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                preferences
                        .edit()
                        .putInt("theme", selectedPosition)
                        .apply();
            restartApp();
            })
            .show();
    return true;
}
        return false;
    }





    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @Nullable Intent data) {
        if (requestCode == LOGIN && resultCode == RESULT_OK) {
            binding.btnLogin.setVisibility(View.GONE);

            isLoggedIn = true;
            binding.swipeRefreshLayout.setEnabled(false);
            UserWrapper user = ((PICTAttendance) getApplication()).getUser();
            this.list = createList(user.getSubjectList());
            adapter.submitList(user.getSubjectList());
            calculateTotal(list, false, false);
            invalidateOptionsMenu();
            storePref(Constants.NAME, user.getName());
            WidgetUtils.updateWidgets(this);
            String year = user.getYear().replace(" ", " • ");
            setHeader(user.getName(), year);
            storePref(Constants.YEAR, year);
            TransitionManager.beginDelayedTransition(binding.parent);
            binding.headerLayout.header.setVisibility(View.VISIBLE);
            binding.emptyItemViewstub.setVisibility(View.GONE);
            binding.categoryList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSelected(List<String> chips) {

        List<Subject> subjects = new ArrayList<>();
        if (chips.isEmpty()) {
            adapter.submitList(createList(list));
            calculateTotal(list, false, true);
            return;
        }
        for (Subject subject: list) {
            if (chips.contains(subject.getCategory()))
                subjects.add(new Subject(subject));
        }
        calculateTotal(subjects, false, true);
        adapter.submitList(subjects);

    }



    @Override
    public void buttonClicked(List<Subject> list) {
        calculateTotal(list, true, false);
    }

    @Override
    public void onSnackBarAction() {
        viewModel.cancelCall();
        binding.snackbar.dismiss();
        binding.swipeRefreshLayout.setRefreshing(false);
    }





}
