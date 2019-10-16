package com.piyush.pictattendance.ui.activities.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.piyush.pictattendance.utils.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final /* synthetic */ class LoadingAnimation implements Runnable {
    private final LoadingView arg$1;

    LoadingAnimation(LoadingView placeholderLoadingView) {
        this.arg$1 = placeholderLoadingView;
    }

    public final void run() {
        final LoadingView view = this.arg$1;
        if (ViewCompat.isAttachedToWindow(view) && ViewCompat.isLaidOut(view)) {
            view.stopAnimationAfterCurrentRun = false;
            if (view.animatorSet != null) {
                view.animatorSet.start();
                return;
            }
            Collection arrayList = new ArrayList();
            if (view.children == null) {
                view.children = Utils.getAllDescendants(view);
                List<View> filteredList = new ArrayList<>();
                for(View view1 : view.children)
                {
                    if(view1 instanceof AppCompatImageView)
                        filteredList.add(view1);
                }
                view.children = filteredList;
            }
            for (View view2 : view.children) {
                view2.animate().alpha(1.0f);
                float relativeTop = ((float) Utils.getRelativeTop(view2, view)) / ((float) view.getHeight());
                float relativeLeft = ((float) Utils.getRelativeLeft(view2, view)) / ((float) view.getWidth());
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view2, "alpha", 1.0f, 0.25f, 1.0f);
                ofFloat.setInterpolator(LoadingView.INTERPOLATOR);
                ofFloat.setDuration(1600);
                ofFloat.setStartDelay((long) Math.round((relativeTop + relativeLeft) * 550.0f));
                arrayList.add(ofFloat);
            }
            view.animatorSet = new AnimatorSet();
            view.animatorSet.playTogether(arrayList);
            view.animatorSet.addListener(new AnimatorListenerAdapter() {
                public final void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (!view.stopAnimationAfterCurrentRun) {
                        animator.start();
                    }
                }
            });
            view.animatorSet.start();
            return;
        }
        Log.w("PlaceholderLoadingView", "View is either not attached or not laid out. Aborting animation.");
    }
}