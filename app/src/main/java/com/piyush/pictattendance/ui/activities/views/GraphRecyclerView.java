package com.piyush.pictattendance.ui.activities.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.piyush.pictattendance.R;
import com.piyush.pictattendance.utils.ColorUtil;
import com.piyush.pictattendance.utils.Utils;

import okhttp3.internal.Util;

public class GraphRecyclerView extends RecyclerView {
    TextPaint textPaint;
    private static  float MARGIN = 8;
    private static final float TEXT_SIZE = 10;
    float dateHeight, monthHeight;
    public GraphRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public GraphRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GraphRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        textPaint = new TextPaint();
        MARGIN = Utils.convertDpToPixel(MARGIN,context);
        textPaint.setColor(ContextCompat.getColor(context,R.color.colorTextGrey2nd));
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextSize(Utils.spToPx(12, context));
        dateHeight = textPaint.getFontMetrics().bottom;
        Log.d("HEIGHT", "GraphRecyclerView: "+ String.valueOf(dateHeight));
        textPaint.setTextSize(Utils.spToPx(13, context));
        monthHeight = textPaint.getFontMetrics().bottom;
        Log.d("MONTH HEIGHT", "GraphRecyclerView: "+ String.valueOf(monthHeight));

        textPaint.setTypeface(Typeface.SANS_SERIF);
        textPaint.setTextSize(Utils.spToPx(TEXT_SIZE, context));
        textPaint.setAntiAlias(true);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawText("max",getWidth()-2*MARGIN,Utils.convertDpToPixel(24,getContext())-textPaint.getFontMetrics().bottom, textPaint);
        canvas.drawText("min",getWidth()-2*MARGIN,getBottom()-dateHeight-monthHeight-MARGIN, textPaint);

    }
}
