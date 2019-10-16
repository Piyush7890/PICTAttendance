package com.piyush.pictattendance.ui.activities.views;

import android.animation.AnimatorSet;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Collection;

public class LoadingView extends FrameLayout {
    public static final LinearInterpolator INTERPOLATOR = new LinearInterpolator();
    public AnimatorSet animatorSet;
    public Collection<View> children;
    public boolean stopAnimationAfterCurrentRun;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public LoadingView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
@Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onVisibilityChanged( @NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(visibility==VISIBLE)
            startAnimation();
        else if(visibility==GONE)
            stopAnimation();

    }

    protected void onDetachedFromWindow() {
        stopAnimation();

        super.onDetachedFromWindow();
    }

    void startAnimation()
    {
        postOnAnimationDelayed(new LoadingAnimation(this), 250);

    }

    void stopAnimation()
    {
        this.stopAnimationAfterCurrentRun = true;
        if (this.animatorSet != null) {
            this.animatorSet.cancel();
        }
    }
}