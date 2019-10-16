package com.piyush.pictattendance.BindingAdapters;

import android.databinding.BindingAdapter;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.piyush.pictattendance.ui.activities.views.RefreshLayout;
import com.piyush.pictattendance.ui.activities.views.Chip;

public class ButtonBindingAdapter {

    @BindingAdapter("clipToCircle")
   public static void clipToCircle(View v,Boolean clip)
    {
        v.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0,0,view.getWidth(),view.getHeight());
            }
        });
        v.setClipToOutline(clip);
    }

    @BindingAdapter("checkedColor")
    public static void setCheckedColor(Chip checkableChipView, int color)
    {
        checkableChipView.setColor(color);
    }


    @BindingAdapter("enabled")
    public static void setEnabled(RefreshLayout layout, boolean enable)
    {
        layout.setEnabled(enable);
    }


}
