package com.piyush.pictattendance.ui.activities.views;

import android.content.Context;

import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;

public class SlidingImageView extends AppCompatImageView {

    private int CENTER_X;
    private float initialX;
    private int dragDistance;
    private float initialY;



    private int CENTER_Y;
    public SlidingImageView(Context context) {
        super(context);
    }

    public SlidingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        CENTER_X = getLeft()+w/2;
        CENTER_Y = getTop()+h/2;
        dragDistance = getWidth()/2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                initialX = event.getRawX();
                initialY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                translateBy(event.getRawX(),event.getRawY());
                //initialX = event.getX();
                //initialY = event.getY();
                break;
            case MotionEvent.ACTION_UP:


            case MotionEvent.ACTION_CANCEL:
                if(getTranslationX()!=0 && getTranslationY()!=0)
               animate().translationX(0)
                       .translationY(0)
                       .setInterpolator(new OvershootInterpolator())
                       .setDuration(150)
                       .start();
                break;
        }

        return true;
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
    }

    private void translateBy(float x, float y) {

        if(x==0 && y==0)
        {
            return;
        }



        float dragFractionX = (float) Math.log10(1 + (Math.abs(x-initialX) / dragDistance));
        float dragFractionY = (float) Math.log10(1 + (Math.abs(y-initialY) / dragDistance));

        float dragToX = dragFractionX * dragDistance;
        float dragToY = dragFractionY * dragDistance;

        if(x-initialX<0)
            dragToX*=-1;
        if(y-initialY<0)
            dragToY*=-1;

        setTranslationX(dragToX);
        setTranslationY(dragToY);




    }
}
