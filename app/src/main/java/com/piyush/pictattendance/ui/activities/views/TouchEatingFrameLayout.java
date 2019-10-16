package com.piyush.pictattendance.ui.activities.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchEatingFrameLayout extends FrameLayout {
    public TouchEatingFrameLayout( @NonNull Context context) {
        super(context);
    }

    public TouchEatingFrameLayout( @NonNull Context context,  @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchEatingFrameLayout( @NonNull Context context,  @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return true;
    }
}
