package com.piyush.pictattendance.ui.activities.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.piyush.pictattendance.R;
import com.piyush.pictattendance.utils.ColorUtil;
import com.piyush.pictattendance.utils.Utils;

public class GradientRecyclerView extends RecyclerView {
    Drawable start, end;

    public GradientRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public GradientRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GradientRecyclerView);
        int color = array.getColor(R.styleable.GradientRecyclerView_gradientColorrr, Color.WHITE);
        start = ColorUtil
                .makeCubicGradientScrimDrawable(color,
                        4,
                        Gravity.START);
        end = ColorUtil
                .makeCubicGradientScrimDrawable(color,
                        4,
                        Gravity.END);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        int width = Math.round(Utils.convertDpToPixel(64, getContext()));
        start.setBounds(0,0,width,getHeight());
        end.setBounds(getRight()-width,0,getRight(),getBottom());
        start.draw(canvas);
        end.draw(canvas);
    }

    @Override
    public void onDraw(Canvas c) {


        super.onDraw(c);
    }


}
