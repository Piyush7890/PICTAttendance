package com.piyush.pictattendance.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ExpandCollapseHelper {

    private Animation expandAnimator, collapseAnimator;
    private ViewGroup view;

    public ExpandCollapseHelper(ViewGroup view)
    {

        this.view = view;
    }


    public void expand() {
        {
            view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final int targetHeight = view.getMeasuredHeight();
            view.getLayoutParams().height = 1;
            view.requestLayout();
            view.setVisibility(View.VISIBLE);
            if (expandAnimator == null) {
                expandAnimator = new Animation() {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        view.getLayoutParams().height = interpolatedTime == 1
                                ? ViewGroup.LayoutParams.WRAP_CONTENT
                                : (int) (targetHeight * interpolatedTime);
                        view.requestLayout();
                        setTranslation(view,-targetHeight*interpolatedTime);


                    }

                    @Override
                    public boolean willChangeBounds() {
                        return true;
                    }
                };
                expandAnimator.setAnimationListener(new AnimationListener() {
                                                @Override
                                                public void onAnimationStart(Animation animation) {
                                                    view.setHasTransientState(true);
                                                }

                                                @Override
                                                public void onAnimationEnd(Animation animation) {
                                                    view.setHasTransientState(false);
                                                }
                                            }
                );
                // a.setDuration(350L);
                expandAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                expandAnimator.setDuration((int) (targetHeight / view.getContext().getResources().getDisplayMetrics().density));
            }
            expandAnimator.cancel();
            view.post(() -> view.startAnimation(expandAnimator));

        }
    }


    void setTranslation(ViewGroup view, float translation)
    {
        for(int i=0;i<view.getChildCount();i++)
        {
            View v = view.getChildAt(i);
            if(v instanceof ViewGroup)
                setTranslation(((ViewGroup) v),translation);
            else {
                v.setTranslationY(translation);
            }
        }
    }


    public void collapse()
        {
            final int initialHeight = view.getMeasuredHeight();
            if (collapseAnimator == null) {
                collapseAnimator = new Animation() {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {

                        if (interpolatedTime == 1) {
                            view.setVisibility(View.GONE);
                        } else {
                            view.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                            view.requestLayout();
                            setTranslation(view,initialHeight*interpolatedTime);

                        }
                    }

                    @Override
                    public boolean willChangeBounds() {
                        return true;
                    }
                };
                collapseAnimator.setAnimationListener(new AnimationListener(){
                    @Override
                    public void onAnimationStart(Animation animation) {
                        view.setHasTransientState(true);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setHasTransientState(false);
                    }
                });

                collapseAnimator.setDuration(200L);
            }
            collapseAnimator.cancel();
            view.post(() -> view.startAnimation(collapseAnimator));

        }
}
