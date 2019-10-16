package com.piyush.pictattendance.ui.activities.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.piyush.pictattendance.R;
import com.piyush.pictattendance.utils.ColorUtil;

public class GradientFrameLayout extends FrameLayout {
    public GradientFrameLayout( @NonNull Context context) {
        this(context,null);
    }

    public GradientFrameLayout(@NonNull Context context,  @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GradientFrameLayout( @NonNull Context context,  @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GradientFrameLayout);
         int color = array.getColor(R.styleable.GradientFrameLayout_gradientColorr,Color.TRANSPARENT);
         int gravity = array.getInt(R.styleable.GradientFrameLayout_gravity, 1);
         array.recycle();
         //long t = System.currentTimeMillis();

        setBackground(ColorUtil
                .makeCubicGradientScrimDrawable(color,
                        4,
                        getGravity(gravity)));


    }


    private int getGravity(int gravity)
    {
        switch (gravity)
        {
            case 0:
                return Gravity.START;
            case 1:
                return Gravity.TOP;
            case 2:
                return  Gravity.END;
            case 3:
                return Gravity.BOTTOM;
                default:
                    return Gravity.START;
        }

    }
}
