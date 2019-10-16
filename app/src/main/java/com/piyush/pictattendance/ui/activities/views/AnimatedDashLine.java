package com.piyush.pictattendance.ui.activities.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.piyush.pictattendance.R;

public class AnimatedDashLine extends View {
    RectF bounds ;
    Paint paint;
    private float radius;
    private float  strokeWidth;
    private boolean isRunning = false;

    public AnimatedDashLine(Context context) {
        this(context, null);
    }

    public AnimatedDashLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimatedDashLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public AnimatedDashLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.makeMeasureSpec(widthMeasureSpec,MeasureSpec.EXACTLY);
        int height = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.EXACTLY);
        bounds.set(strokeWidth,strokeWidth, ((float) width)-strokeWidth, ((float) height)-strokeWidth);
        setMeasuredDimension(width,height);

    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    private void init(Context context, AttributeSet attributeSet)
    {
        TypedArray array = context.obtainStyledAttributes(attributeSet,R.styleable.AnimatedDashLine);
        int color = array.getColor(R.styleable.AnimatedDashLine_dashLineColorr,Color.TRANSPARENT);
        array.recycle();
        radius = convertDpToPixel(8, getContext());
        strokeWidth = convertDpToPixel(3,getContext());
        paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setPathEffect(new DashPathEffect(new float[]{20.0f, 15.0f}, 0.0f));
        paint.setStyle(Paint.Style.STROKE);
        bounds = new RectF();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
     //   canvas.drawRect(strokeWidth,strokeWidth,canvas.getWidth()-strokeWidth,canvas.getHeight()-strokeWidth,paint);
        canvas.drawRoundRect(strokeWidth,strokeWidth,canvas.getWidth()-strokeWidth,canvas.getHeight()-strokeWidth,radius,radius,paint);
    }

    private static  class animator implements AnimatorUpdateListener{

        private AnimatedDashLine line;
        private ValueAnimator valueAnimator;

        public animator(AnimatedDashLine line, ValueAnimator valueAnimator)
        {

            this.line = line;
            this.valueAnimator = valueAnimator;
        }
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if(line.isRunning)
            {
                float[] fArr = new float[]{20.0f, 15.0f};
                float value = ((float) animation.getAnimatedValue());
                line.paint.setPathEffect(new DashPathEffect(fArr,value));
                line.invalidate();
                return;
            }
            valueAnimator.cancel();

        }
    }

    public final void start()
    {
        isRunning=true;
        ValueAnimator ofFloat = ObjectAnimator.ofFloat(0.0f,-175.0f);
        ofFloat.setDuration(4000);
        ofFloat.addUpdateListener(new animator(this, ofFloat));
        ofFloat.setInterpolator(new LinearInterpolator());
        ofFloat.setRepeatCount(ValueAnimator.INFINITE);
        ofFloat.setRepeatMode(ValueAnimator.RESTART);
        ofFloat.start();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isRunning=false;
    }
}
