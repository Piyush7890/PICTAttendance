package com.piyush.pictattendance.ui.activities.views;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.Gravity;

import com.piyush.pictattendance.R;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

public class CustomTickerView extends TickerView {
    public CustomTickerView(Context context) {
        this(context, null);
    }

    public CustomTickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {


        setCharacterLists(TickerUtils.provideNumberList());
        setTypeface(ResourcesCompat.getFont(context,  R.font.work_sans_medium));
        setAnimateMeasurementChange(false);
        setGravity(Gravity.END);
    }
}
