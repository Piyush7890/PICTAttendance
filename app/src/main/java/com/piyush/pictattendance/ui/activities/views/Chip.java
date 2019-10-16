package com.piyush.pictattendance.ui.activities.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.Interpolator;
import android.widget.Checkable;

import com.piyush.pictattendance.R;
import com.piyush.pictattendance.utils.AnimUtils;


import static android.graphics.Paint.ANTI_ALIAS_FLAG;


public class Chip extends View implements Checkable
{

    private int color = 0xffff00ff;

    public void setColor(int value) {
        if (color != value) {
            color = value;
            dotPaint.setColor(value);
            postInvalidateOnAnimation();
        }
    }

    private StaticLayout textLayout=null;


    private Integer selectedTextColor = null;

    private CharSequence text = "";

    public void setText(CharSequence value)
    {
        text= value;
        requestLayout();
    }

    private  boolean showIcons = true;

    public  void setShowIcons(boolean value)
    {
        if (showIcons != value) {
            showIcons  = value;
            requestLayout();
        }
    }

    private float progress = 0f;

    private void setProgress(float value)
    {
        if(progress!=value)
        {
            progress = value;
            postInvalidateOnAnimation();

        }
    }


   private final long SELECTING_DURATION = 350L;
    private final long DESELECTING_DURATION = 250L;

    private int padding;
    private float newProgress;
    Interpolator interpolator ;


    public Chip(Context context) {
        this(context, null);
    }

    public Chip(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Chip(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CharSequence getText()
    {
        return  text;
    }

    private void init(Context context, AttributeSet attributeSet) {

        TypedArray  a = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.Chip,
                R.attr.chipStyle,
                R.style.EventFilters
        );

        interpolator = AnimUtils.getFastOutSlowInInterpolator(context);
        outlinePaint = new Paint(ANTI_ALIAS_FLAG);
        outlinePaint.setColor(a.getColor(R.styleable.Chip_android_strokeColor,Color.TRANSPARENT));
        outlinePaint.setStrokeWidth(a.getDimension(R.styleable.Chip_outlineWidth,0));
        outlinePaint.setStyle(Paint.Style.STROKE);
        defaultTextColor = a.getColor(R.styleable.Chip_android_textColor, Color.TRANSPARENT);
        dotPaint = new Paint(ANTI_ALIAS_FLAG);
        color = a.getColor(R.styleable.Chip_dotColor, Color.TRANSPARENT);
        dotPaint.setColor(color);
        textPaint = new TextPaint(ANTI_ALIAS_FLAG);
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setLetterSpacing(-0.04f);
        textPaint.setColor(defaultTextColor);
        //textPaint.setLetterSpacing(0.01f);
        textPaint.setTextSize(a.getDimension(R.styleable.Chip_android_textSize, 0f));
        progressAnimator = ValueAnimator.ofFloat();
        selectedTextColor = a.getColor(R.styleable.Chip_selectedTextColor, Color.TRANSPARENT);

        clear = a.getDrawable(R.styleable.Chip_clearIcon);
        if (clear != null) {
            clear.setBounds(-clear.getIntrinsicWidth()/2,
                    -clear.getIntrinsicHeight()/2,
                    clear.getIntrinsicWidth()/2,clear.getIntrinsicHeight()/2
                    );
        }


        touchFeedback = a.getDrawable(R.styleable.Chip_foreground);
        if (touchFeedback != null) {
            touchFeedback.setCallback(this);
        }
        padding = a.getDimensionPixelSize(R.styleable.Chip_android_padding, 0);
        showIcons = a.getBoolean(R.styleable.Chip_showIcons, true);
        a.recycle();
        setClipToOutline(true);

    }

    Float lerp( Float a, Float b,  Float t) {
    return a + (b - a) * t;
}

    @Override
    protected void onDraw(Canvas canvas) {

        float strokeWidth = outlinePaint.getStrokeWidth();
        float iconRadius = clear.getIntrinsicWidth()/2f;
        float halfStroke = strokeWidth/2f;
        float rounding = (getHeight()-strokeWidth) / 2f;

        if(progress<1f)
        {
            canvas.drawRoundRect(
                    halfStroke,
                    halfStroke,
                    getWidth() - halfStroke,
                    getHeight() - halfStroke,
                    rounding,
                    rounding,
                    outlinePaint
            );
        }


        if (showIcons) {
            float dotRadius = lerp(
                    strokeWidth + iconRadius,
                    ((float) getWidth()),
                    progress
            );
            canvas.drawCircle(strokeWidth + padding + iconRadius,
                    getHeight() / 2f,
                    dotRadius,
                    dotPaint);
        }
        else {
            canvas.drawRoundRect(
                    halfStroke,
                    halfStroke,
                    getWidth() - halfStroke,
                    getHeight() - halfStroke,
                    rounding,
                    rounding,
                    dotPaint
            );
        }
        float textX = showIcons ?
            lerp(
                    strokeWidth + padding + clear.getIntrinsicWidth() + padding,
                    strokeWidth + padding * 2f,
                    progress
            )
        :
            strokeWidth + padding * 2f;
        Integer  selectedColor = selectedTextColor;
        textPaint.setColor((selectedColor != null && selectedColor != 0 && progress > 0)?
            ColorUtils.blendARGB(defaultTextColor, selectedColor, progress)
       : defaultTextColor);

        int count = canvas.save();
        canvas.translate(textX,(getHeight()-textLayout.getHeight())/2f);
        textLayout.draw(canvas);

        canvas.restoreToCount(count);
        // Clear icon
        if (showIcons && progress > 0f) {
           count= canvas.save();
            canvas.translate(
                    getWidth() - strokeWidth - padding - iconRadius,
                    getHeight() / 2f
            );
                canvas.scale(progress, progress);
                    clear.draw(canvas);
                canvas.restoreToCount(count);
            }

            touchFeedback. draw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int nonTextWidth = (4 * padding)
                + (int)(2* outlinePaint.getStrokeWidth())
                + (showIcons?clear.getIntrinsicWidth():0);
        int availableTextWidth ;

        switch (MeasureSpec.getMode(widthMeasureSpec))
        {
            case MeasureSpec.EXACTLY:
            {
                availableTextWidth = MeasureSpec.getSize(widthMeasureSpec)- nonTextWidth;
            break;
            }

            case MeasureSpec.AT_MOST:
            {
                availableTextWidth = MeasureSpec.getSize(widthMeasureSpec)- nonTextWidth;
                break;
            }
            case MeasureSpec.UNSPECIFIED:
            {
                availableTextWidth = Integer.MAX_VALUE;
                break;
            }
            default:
            {
                availableTextWidth = Integer.MAX_VALUE;
            }
        }
        createLayout(availableTextWidth);
        int w = nonTextWidth + ((int) textLayout.getLineWidth(0));
        int h = padding + textLayout.getHeight() + padding;
        setMeasuredDimension(w, h);
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, w, h, h / 2f);

            }
        });

        touchFeedback.setBounds(0, 0, w, h);
    }





    private void createLayout(int textWidth) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
           textLayout = StaticLayout.Builder.obtain(text,
                   0,
                   text.length(),
                   textPaint,
                   textWidth).build();
        }
        else
        {
            textLayout = new StaticLayout(text,
                    textPaint,
                    textWidth,
                    Layout.Alignment.ALIGN_NORMAL,
                    1f,
                    0f,

                    true);
        }
    }


    private Paint outlinePaint;
    private TextPaint textPaint;
    private Paint dotPaint;
    private Drawable clear;
    private Drawable touchFeedback;
    private ValueAnimator progressAnimator  = null;
    private int defaultTextColor;






ValueAnimator.AnimatorUpdateListener updateListener = animation ->
        setProgress((float) animation.getAnimatedValue());


public interface Listener
{
     void onClick(Chip chip, boolean checked);
}

    public void animateCheckAndInvoke(boolean checked, Listener listener)
    {

         newProgress = checked?1f:0f;
        if(newProgress!=progress)
        {

           progressAnimator.cancel();
            progressAnimator = ValueAnimator.ofFloat(progress,newProgress);
            progressAnimator.addUpdateListener(updateListener);
            progressAnimator.setInterpolator(interpolator);
            progressAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if(listener!=null)
                    listener.onClick(Chip.this,checked);
                }
            });

            progressAnimator.setDuration(checked? SELECTING_DURATION : DESELECTING_DURATION);
           progressAnimator.start();
        }
    }





    @Override
    public void setChecked(boolean checked) {
        newProgress = checked?1f:0f;
        setProgress(newProgress);
    }

    public boolean isChecked() {
        return newProgress==1f;
    }

    @Override
    public void toggle() {
       setProgress((progress==0f)?1f:0f);
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return super.verifyDrawable(who) || who == touchFeedback;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        touchFeedback.setState(getDrawableState());
    }

    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        touchFeedback.setHotspot(x,y);
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        touchFeedback.jumpToCurrentState();
    }
}