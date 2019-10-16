package com.piyush.pictattendance.ui.activities.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

public class ClippableLayout extends FrameLayout {
    public Path path;

    public ClippableLayout(Context context) {
        super(context);
    }

    public ClippableLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ClippableLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    protected void dispatchDraw(Canvas canvas) {
        if (this.path == null) {
            super.dispatchDraw(canvas);
            return;
        }
        int save = canvas.save();
        canvas.clipPath(this.path);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);
    }

    public void updateClipBoundsAndCornerRadius(Rect rect, float f) {
        updateClipBoundsAndCornerRadius((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bottom, f);


    }

    public void updateClipBoundsAndCornerRadius(float f, float f2, float f3, float f4, float f5) {
        updateClipBoundsAndCornerRadius(new RectF(f, f2, f3, f4), f5);
    }

    public void updateClipBoundsAndCornerRadius(RectF rectF, float f) {
        if (this.path == null) {
            this.path = new Path();
        }
        this.path.reset();
        this.path.addRoundRect(rectF, f, f, Path.Direction.CW);
        this.path.close();
        invalidate();
    }
}