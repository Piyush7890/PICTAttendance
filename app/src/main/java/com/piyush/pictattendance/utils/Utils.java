package com.piyush.pictattendance.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import com.piyush.pictattendance.R;
import com.piyush.pictattendance.model.Category;
import com.piyush.pictattendance.model.MenuItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static Interpolator interpolator;
    private static List<Category> categoryList;
    private static List<MenuItem> items;



    public static List<MenuItem> getMenuItems()
    {
        if(items ==null)
        {
            items = new ArrayList<>();
            items.add(new MenuItem(R.drawable.ic_exit_to_app_black_24dp, "Log Out"));
            items.add(new MenuItem(R.drawable.ic_open_in_browser_black_24dp, "Open in Browser"));
            items.add(new MenuItem(R.drawable.ic_timeline_black_24dp, "View Graph"));
            items.add(new MenuItem(R.drawable.ic_outline_round, "Change Theme"));
            items.add(new MenuItem(R.drawable.ic_settings_black_24dp, "Settings"));
            items.add(new MenuItem(R.drawable.ic_feedback_black_24dp, "Provide Feedback"));
            items.add(new MenuItem(R.drawable.ic_star_border_black_24dp, "Rate On PlayStore"));
        }
            return items;
    }




    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public static void rateOnPlayStore( Context context) {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
    public static void setLightStatusBar(Activity context, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            context.getWindow().setStatusBarColor(color);
            final View view = context.getWindow().getDecorView();
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static Interpolator getFastOutSlowIn()
    {
        if(interpolator==null)
            interpolator = new FastOutSlowInInterpolator();
        return interpolator;
    }
    public static String toUpperCase(String text)
    {
        StringBuilder result = new StringBuilder(text.length());
        String words[] = text.split(" ");
        for (String word : words) {
            if(!word.isEmpty())
            result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");

        }
        return result.toString();
    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private static void createCategoryList()
    {
        categoryList = new ArrayList<>();
        categoryList.add(new Category(1,"Lecture", Color.parseColor("#1db8d2")));
        categoryList.add(new Category(2,"Practical", Color.parseColor("#8da0fc")));
        categoryList.add(new Category(3,"Tutorial", Color.parseColor("#e8bc4f")));
        categoryList.add(new Category(4,"Workshop", Color.parseColor("#94dd6b")));
        categoryList.add(new Category(5,"Unknown", Color.parseColor("#ff9e80")));
    }

    public static String category(String text)
    {
        String category;
        switch (text) {
            case "pr":
                category = "Practical";
                break;
            case "th":
                category = "Lecture";
                break;
            case "tut":
                category = "Tutorial";
                break;
            case "wr":
                category="Workshop";
                break;

            default:
                category = "Unknown";
                break;
        }
        return category;
    }

    public static int getCategoryColor(String Category)
    {
        if(categoryList==null) {
            createCategoryList();
        }
        switch (Category)
        {
            case "Practical":
                return categoryList.get(1).getColor();
            case "Lecture":
                return categoryList.get(0).getColor();
            case "Tutorial":
                return categoryList.get(2).getColor();
            case "Workshop":
                return categoryList.get(3).getColor();
            case "Unknown":
                return categoryList.get(4).getColor();
        }
        return Color.WHITE;
    }

    public static int getRelativeTop(View view, ViewGroup viewGroup) {
        if (view.getParent() == viewGroup) {
            return view.getTop();
        }
        return getRelativeTop((View) view.getParent(), viewGroup) + view.getTop();
    }

    public static int getRelativeLeft(View view, ViewGroup viewGroup) {
        if (view.getParent() == viewGroup) {
            return view.getLeft();
        }
        return getRelativeLeft((View) view.getParent(), viewGroup) + view.getLeft();
    }


    public static List<View> getAllDescendants(ViewGroup viewGroup) {
        List<View> arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        arrayList2.add(viewGroup);
        while (!arrayList2.isEmpty()) {
            View view = (View) arrayList2.remove(0);
            arrayList.add(view);
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup2 = (ViewGroup) view;
                int childCount = viewGroup2.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    arrayList2.add(viewGroup2.getChildAt(i));
                }
            }
        }
        return arrayList;
    }

}
