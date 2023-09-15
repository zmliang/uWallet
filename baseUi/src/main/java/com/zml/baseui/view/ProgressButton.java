package com.zml.baseui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.zml.baseui.DensityUtil;


public class ProgressButton extends View {

    private final int MIN_WIDTH = DensityUtil.getPixels(50,getContext());
    private final int MIN_HEIGHT = DensityUtil.getPixels(10,getContext());

    private int defaultColor = Color.RED;

    private RectF rectF = new RectF();

    private Paint paint = new Paint();

    private ColorAnimationDrawable colorBgAnimDrawable = new ColorAnimationDrawable(getContext());

    public void startColorBgAnim(){
        colorBgAnimDrawable.startAnimate();
    }

    public ProgressButton(Context context) {
        this(context,null);
    }

    public ProgressButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }
    public void startProgress(){
        colorBgAnimDrawable.startAnimate();
    }


    private void init(Context context,AttributeSet attr){
        paint.setAntiAlias(true);
        paint.setDither(true);
        //colorBgAnimDrawable.setCallback(this);
        setBackground(colorBgAnimDrawable);
        Log.i("zml","初始化");
        //colorBgAnimDrawable.startAnimate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i("zml","onDetachedFromWindow");
        colorBgAnimDrawable.destroy();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w_mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int h_mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int realWidth = width;
        int realHeight = height;

        if (w_mode == MeasureSpec.AT_MOST){
            realWidth = MIN_WIDTH;
        }
        if (h_mode == MeasureSpec.AT_MOST){
            realHeight = MIN_HEIGHT;
        }

        Log.i("zml","宽="+realWidth+"; 高="+realHeight);
        setMeasuredDimension(realWidth,realHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int minSide = Math.min(getMeasuredHeight(),getMeasuredWidth());
        //.draw(canvas);

        /**
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        rectF.set(0,0,getMeasuredWidth()/2,getMeasuredHeight()/2);
        paint.setColor(defaultColor);
        canvas.drawRect(rectF,paint);

        rectF.set(getMeasuredWidth()/2,getMeasuredHeight()/2,getMeasuredWidth(),getMeasuredHeight());
        canvas.drawRoundRect(rectF,getMeasuredWidth()/4,getMeasuredWidth()/4,paint);

        paint.setColor(Color.WHITE);
        int strokeW = DensityUtil.getPixels(5,getContext());
        paint.setStrokeWidth(strokeW);
        canvas.drawLine(0,0,getMeasuredWidth(),getMeasuredHeight(),paint);

        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        rectF.set(strokeW/2,strokeW/2,minSide-strokeW/2,minSide-strokeW/2);
        canvas.drawArc(rectF,0,360,true,paint);
         */
    }
}
