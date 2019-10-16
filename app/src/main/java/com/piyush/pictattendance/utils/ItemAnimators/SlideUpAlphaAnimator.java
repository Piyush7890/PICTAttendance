package com.piyush.pictattendance.utils.ItemAnimators;


import android.view.View;

import com.piyush.pictattendance.utils.Utils;

public class SlideUpAlphaAnimator extends SlideAlphaAnimator<SlideUpAlphaAnimator> {
    protected float getAnimationTranslationX(View view) {
        return 0.0f;
    }

    public static SlideUpAlphaAnimator create() {
        return new SlideUpAlphaAnimator()
                .withInterpolator(Utils.getFastOutSlowIn())
                .withAddDuration(250)
                .withRemoveDuration(250);
    }

    public SlideUpAlphaAnimator() {
        super(0);
    }

    protected float getAnimationTranslationY(View view) {
        return (float) SlideAlphaAnimator
                .dpToPx(32.0f, view.getContext());
    }
}