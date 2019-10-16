package com.piyush.pictattendance.ui.activities.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.piyush.pictattendance.R;


public class FadingSnackbar extends FrameLayout {
    private TextView message;
    Button action;
    android.os.Handler handler;
    private View root;
    private  long ENTER_DURATION = 300L;
    private long EXIT_DURATION = 200L;
    private long SHORT_DURATION = 1_500L;
    private long LONG_DURATION = 2_750L;
    private Listener listener;

    public FadingSnackbar(@NonNull Context context) {
        this(context,null);
    }

    public FadingSnackbar( @NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FadingSnackbar( @NonNull Context context,@Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public void setListener(Listener listener)
    {

        this.listener = listener;
    }

    private void init(Context context, AttributeSet attributeSet)
    {


        setVisibility(GONE);
        handler = new android.os.Handler();
        root = LayoutInflater.from(getContext())
                .inflate(R.layout.fading_snackbar_layout,
                        this,
                        true);
        action = findViewById(R.id.action);
        message= root.findViewById(R.id.snackbar_text);
        TypedArray array = context.obtainStyledAttributes(attributeSet,R.styleable.FadingSnackbar);
        String buttonText = array.getString(R.styleable.FadingSnackbar_buttonText);
        array.recycle();
        if(buttonText!=null) {
            action.setText(buttonText);
            action.setVisibility(VISIBLE);
            action.setOnClickListener(v -> listener.onSnackBarAction());

        }

    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
    }

    public void show(String message, LENGTH time, boolean indefinite)
    {
        this.message.setText(message);
        setAlpha(0f);
        setScaleX(0.90f);
        setScaleY(0.90f);
        setVisibility(VISIBLE);
        animate().setDuration(ENTER_DURATION).alpha(1f).scaleY(1f).scaleX(1f).start();

        if(!indefinite) {
        long Time = 0L;
        switch (time) {
            case LENGTH_SHORT:
                Time = SHORT_DURATION;
                break;

            case LENGTH_LONG:
                Time = LONG_DURATION;
                break;
        }

            handler.postDelayed(this::dismiss,ENTER_DURATION+ Time);

        }
    }

    public void dismiss() {
        if (getVisibility() == VISIBLE) {

            animate()
                    .alpha(0f)
                    .scaleY(0.90f)
                    .scaleX(0.90f)
                    .setDuration(EXIT_DURATION).withEndAction(() -> setVisibility(GONE)).start();
        }
    }

    public enum LENGTH
    {
        LENGTH_SHORT,LENGTH_LONG,INDEFINITE
    }

    public interface Listener
    {
        void onSnackBarAction();
    }

}
