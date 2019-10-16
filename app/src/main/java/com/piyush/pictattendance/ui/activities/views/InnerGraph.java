package com.piyush.pictattendance.ui.activities.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import com.piyush.pictattendance.R;
import com.piyush.pictattendance.utils.Utils;


public class InnerGraph extends View {


    private  float MARGIN_TOP = 24;
    private float TEXT_SIZE = 13;
    private float MARGIN_BOTTOM = 8;
    private float TEXT_MARGIN =3;
    private float DIVIDER_WIDTH = 1;
    private float DOT_RADIUS = 2;

    Path graphPath ;
    Path fillPath ;
    TextPaint percentPaint;
    Paint fillPaint;
    Paint strokePaint;
    Paint dividerPaint;
    int colors[];
    private double[] points;
    private double min;
    private double max;
    public static final int NON_EXISTENT = Integer.MAX_VALUE;
    Shader shader;

    public InnerGraph(Context context) {
        this(context, null);
    }

    public InnerGraph(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public InnerGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {

        MARGIN_BOTTOM = Utils.convertDpToPixel(MARGIN_BOTTOM,context);
        MARGIN_TOP = Utils.convertDpToPixel(MARGIN_TOP, context);
        TEXT_SIZE = Utils.convertDpToPixel(TEXT_SIZE, context);
        TEXT_MARGIN = Utils.convertDpToPixel(TEXT_MARGIN, context);
        DIVIDER_WIDTH = Utils.convertDpToPixel(DIVIDER_WIDTH, context);
        DOT_RADIUS = Utils.convertDpToPixel(DOT_RADIUS, context);


        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.InnerGraph);
        boolean isDark = array.getBoolean(R.styleable.InnerGraph_isDark, false);
        array.recycle();
        int strokeColor;
        int strokeShadowColor;
        int dividerColor;
        int percentColor;
        if(isDark)
        {
            percentColor = Color.parseColor("#bfbfbf");
            dividerColor = Color.parseColor("#05ffffff");
            strokeShadowColor = Color.parseColor("#80ffffff");
            colors = new int[]{
                    Color.argb(50,255,255,255),
                    Color.argb(0,255,255,255)
            };
            strokeColor = Color.WHITE;
        }

        else
        {
            percentColor = Color.parseColor("#424242");
            dividerColor = Color.parseColor("#05000000");
            strokeShadowColor = Color.parseColor("#80000000");
            strokeColor = Color.parseColor("#DE000000");
            colors = new int[]{
                    Color.argb(50,33,33,33),
                    Color.argb(0,0,0,0)
            };

        }

        min = max = 0;
        graphPath = new Path();
        fillPath = new Path();
        percentPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        percentPaint.setTextAlign(Paint.Align.CENTER);
        percentPaint.setTextSize(TEXT_SIZE);
        percentPaint.setStyle(Paint.Style.FILL);
        percentPaint.setColor(percentColor);
        percentPaint.setShadowLayer(2,0,2,strokeShadowColor);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStrokeCap(Paint.Cap.ROUND);
        strokePaint.setColor(strokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setDither(true);
        strokePaint.setStrokeWidth(Utils.convertDpToPixel(2, context));
        strokePaint.setShadowLayer(3,0,3,strokeShadowColor);


        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);


        dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dividerPaint.setStyle(Paint.Style.STROKE);
        dividerPaint.setStrokeWidth(DIVIDER_WIDTH);
        dividerPaint.setColor(dividerColor);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        if(points==null)
            return;
        fillPath.rewind();
        graphPath.rewind();
        if(shader==null)
        {
            shader = new LinearGradient(0f,
                    MARGIN_TOP,
                    0f,
                    getHeight()-MARGIN_BOTTOM,
                    colors[0],
                    colors[1],
                    Shader.TileMode.CLAMP);
            fillPaint.setShader(shader);
        }
        float finalHeight = getHeight()-MARGIN_BOTTOM-MARGIN_TOP;
        float centerHeight = computeHeight(points[1]) ;

        if(points[0]==NON_EXISTENT && points[2]==NON_EXISTENT)
        {
            canvas.drawCircle(
                    getWidth()/2
                    ,centerHeight
                    , DOT_RADIUS
                    , strokePaint);
        }

        else if(points[0]==NON_EXISTENT) {

                float height_l = computeHeight(points[2]);
                fillPath.moveTo(0f + getWidth() / 2, finalHeight + MARGIN_TOP);
                fillPath.lineTo(getWidth() / 2, centerHeight);
                fillPath.lineTo(getWidth(), height_l);
                fillPath.lineTo(getWidth(), finalHeight + MARGIN_TOP);
                fillPath.close();
                graphPath.moveTo(getWidth() / 2, centerHeight);
                graphPath.lineTo(getWidth(), height_l);

        }

        else if(points[2]==NON_EXISTENT)
        {
            float height_f = computeHeight(points[0]);
            fillPath.moveTo(0f+getWidth()/2, finalHeight+MARGIN_TOP);
            fillPath.lineTo(0, finalHeight+MARGIN_TOP);
            fillPath.lineTo(0, height_f);
            fillPath.lineTo(getWidth()/2,centerHeight);
            fillPath.close();
            graphPath.moveTo(0, height_f);
            graphPath.lineTo(getWidth()/2,centerHeight);
        }

        else
        {
            float height_f = computeHeight(points[0]);
            float height_l = computeHeight(points[2]);
            fillPath.moveTo(0,finalHeight+MARGIN_TOP);
            fillPath.lineTo(0, height_f);
            fillPath.lineTo(getWidth()/2, centerHeight);
            fillPath.lineTo(getWidth(), height_l);
            fillPath.lineTo(getWidth(),finalHeight+MARGIN_TOP);
            fillPath.close();
            graphPath.moveTo(0, height_f);
            graphPath.lineTo(getWidth()/2,centerHeight);
            graphPath.lineTo(getWidth(),height_l);
        }
        canvas.drawPath(fillPath, fillPaint);
        canvas.drawPath(graphPath, strokePaint);

        canvas.drawText(String.valueOf(points[1]+"%"),
                getWidth()/2,
                centerHeight
                        -percentPaint.getFontMetrics().bottom
                        -TEXT_MARGIN,
                percentPaint);

        int perHeight = 0;
        for(int i=0;i<5;i++)
        {
            canvas.drawLine(0,
                    getHeight()-MARGIN_BOTTOM-perHeight,
                    getWidth(),
                    getHeight()-MARGIN_BOTTOM-perHeight,
                    dividerPaint);
            perHeight+=finalHeight/4;
        }
        canvas.drawLine(getWidth()/2,centerHeight,getWidth()/2,MARGIN_TOP+finalHeight, dividerPaint);

    }

    public void setPoints(double[] points, double min, double max)
    {

        this.points = points;
        this.min = min;
        this.max = max;
        invalidate();
    }

   private float computeHeight(double point)
    {
        if(min==max)
            return getHeight()-MARGIN_BOTTOM;
        return getHeight()- MARGIN_BOTTOM-((float) (((float) ((point - min) * (getHeight() - MARGIN_TOP - MARGIN_BOTTOM))) / (max - min)));
       // return getHeight()-MARGIN_BOTTOM-((float)((getHeight()-MARGIN_BOTTOM-MARGIN_TOP)*point/100f));
    }

}
