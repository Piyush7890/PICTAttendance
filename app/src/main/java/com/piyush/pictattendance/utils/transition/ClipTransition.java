package com.piyush.pictattendance.utils.transition;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.Rect;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import com.piyush.pictattendance.utils.AnimUtils;
import com.piyush.pictattendance.utils.Utils;

public class ClipTransition extends Visibility {

    public ClipTransition() {
    }

    public ClipTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot,
                             View view,
                             TransitionValues startValues,
                             TransitionValues endValues) {
        float finalElevation = Utils.convertDpToPixel(8, sceneRoot.getContext());
        float translation = Utils.convertDpToPixel(56, sceneRoot.getContext());
        view.setTranslationY(translation);

        int height = view.getHeight();
        clipOutline(view,new Rect(0,0,view.getWidth(),0));
        ValueAnimator animator = ValueAnimator.ofFloat(0f,1f);
        animator.addUpdateListener(animation -> {
            float val = ((float) animation.getAnimatedValue());
            view.setTranslationY(translation * (1-val));
            clipOutline(view,new Rect(0,
                    0,
                    view.getWidth(),
                    Math.round(height*val)));
            view.setElevation(finalElevation * val);


        });
        animator.setDuration(350L);
        animator.setInterpolator(Utils.getFastOutSlowIn());
        return new AnimUtils.NoPauseAnimator(animator);    }

    private void clipOutline(View view, Rect rect) {

        view.setClipBounds(rect);
        view.invalidateOutline();

    }


    @Override
    public Animator onDisappear(ViewGroup sceneRoot,
                                View view,
                                TransitionValues startValues,
                                TransitionValues endValues) {
        float translation = Utils.convertDpToPixel(56, sceneRoot.getContext());
        float initialElevation = Utils.convertDpToPixel(8, sceneRoot.getContext());
        int height = view.getHeight();
        ValueAnimator animator = ValueAnimator.ofFloat(0f,1f);
        animator.addUpdateListener(animation -> {
            float val = ((float) animation.getAnimatedValue());
            view.setTranslationY( translation*val);
            clipOutline(view,new Rect(0,
                    0,
                    view.getWidth(),
                    Math.round(height*(1-val))) );
            view.setElevation(initialElevation * (1-val));
        });
        animator.setDuration(300L);
        animator.setInterpolator(Utils.getFastOutSlowIn());
        return new AnimUtils.NoPauseAnimator(animator);
    }
}
