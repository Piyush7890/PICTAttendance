package com.piyush.pictattendance.utils.ItemAnimators;

import android.content.Context;
import android.support.annotation.Px;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.TypedValue;
import android.view.View;

public abstract class SlideAlphaAnimator<T> extends DefaultItemAnimator<T> {
    private final int itemViewElevation;

    private float getMinAlpha() {
        return 0.0f;
    }

    public long getAddDelay(long j, long j2, long j3) {
        return 0;
    }

    protected abstract float getAnimationTranslationX(View view);

    protected abstract float getAnimationTranslationY(View view);

    public void onMoveFinished(ViewHolder viewHolder) {
    }

    public void onMoveStarting(ViewHolder viewHolder) {
    }

    protected SlideAlphaAnimator(int i) {
        this.itemViewElevation = i;
    }

    public T withRemoveDuration(long j) {
        setRemoveDuration(j);
        return (T) this;
    }

    public T withAddDuration(long j) {
        setAddDuration(j);
        return (T) this;
    }

    @Px
    public static int dpToPx(float f, Context context) {
        return (int) TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }

    public void addAnimationPrepare(ViewHolder viewHolder) {
        ViewCompat.setTranslationX(viewHolder.itemView, getAnimationTranslationX(viewHolder.itemView));
        ViewCompat.setTranslationY(viewHolder.itemView, getAnimationTranslationY(viewHolder.itemView));
        ViewCompat.setAlpha(viewHolder.itemView, getMinAlpha());
        viewHolder.itemView.setElevation(0.0f);
    }

    public ViewPropertyAnimatorCompat addAnimation(ViewHolder viewHolder) {
        return ViewCompat.animate(viewHolder.itemView).translationX(0.0f).translationY(0.0f).alpha(1.0f).setDuration(getMoveDuration()).setInterpolator(getInterpolator());
    }

    public void addAnimationCleanup(ViewHolder viewHolder) {
        ViewCompat.setTranslationX(viewHolder.itemView, 0.0f);
        ViewCompat.setTranslationY(viewHolder.itemView, 0.0f);
        ViewCompat.setAlpha(viewHolder.itemView, 1.0f);
        viewHolder.itemView.setElevation((float) this.itemViewElevation);
    }

    public long getRemoveDelay(long j, long j2, long j3) {
        return j / 2;
    }

    public void removeAnimationPrepare(ViewHolder viewHolder) {
        viewHolder.itemView.setElevation(0.0f);
    }

    public ViewPropertyAnimatorCompat removeAnimation(ViewHolder viewHolder) {
      //  return ViewCompat.animate(viewHolder.itemView).setDuration(getRemoveDuration()).alpha(getMinAlpha()).translationX(getAnimationTranslationX(viewHolder.itemView)).translationY(getAnimationTranslationY(viewHolder.itemView)).setInterpolator(getInterpolator());
        return ViewCompat.animate(viewHolder.itemView).setDuration(getRemoveDuration()).alpha(getMinAlpha()).setInterpolator(getInterpolator());
    }

    public void removeAnimationCleanup(ViewHolder viewHolder) {
        ViewCompat.setTranslationX(viewHolder.itemView, 0.0f);
        ViewCompat.setTranslationY(viewHolder.itemView, 0.0f);
        ViewCompat.setAlpha(viewHolder.itemView, 1.0f);
        viewHolder.itemView.setElevation((float) this.itemViewElevation);
    }
}