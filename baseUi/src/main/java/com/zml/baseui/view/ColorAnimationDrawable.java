package com.zml.baseui.view;

import static android.animation.ValueAnimator.REVERSE;
import static android.graphics.PixelFormat.TRANSLUCENT;
import static android.view.animation.Animation.INFINITE;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

import com.zml.baseui.DensityUtil;

import java.lang.ref.WeakReference;

public class ColorAnimationDrawable extends Drawable {
    private final Paint paint = new Paint();


    public static final int STATUS_IDLE = 0;
    public static final int STATUS_PROGRESS = 1;
    public static final int STATUS_SUCCESS = 2;
    public static final int STATUS_FAILED = 3;

    private ValueAnimator animator;
    private ValueAnimator rateAnimator;

    private ValueAnimator dotAnimator;

    private ValueAnimator rectAnimator;
    private int bgColor = Color.GRAY;

    private RectF rectF = new RectF();

    private RectF resourceRectF = new RectF();

    private float rate = 1.0f;

    private float bgRectRate = 1.0f;

    private float dotRate = 1.0f;
    private WeakReference<Context> contextWeakReference;

    private int status = STATUS_PROGRESS;

    public ColorAnimationDrawable(Context context){
        contextWeakReference = new WeakReference<>(context);
    }

    public void startAnimate(){
        if (status == STATUS_PROGRESS){
            animateDot();
        }
        //animateRect();
        //animateColor();

    }

    public void destroy(){
        if (dotAnimator!=null){
            dotAnimator.cancel();
            dotAnimator.removeAllUpdateListeners();

        }
    }

    private void animateColor(){
        if (animator!=null && animator.isRunning()){
            animator.cancel();
        }
        animator = ValueAnimator.ofArgb(Color.GRAY,Color.GREEN);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());

        animator.addUpdateListener(animation -> {
            bgColor = (int) animation.getAnimatedValue();
            Log.i("zml","value="+bgColor);
            invalidateSelf();
        });
        animator.start();
    }

    private void animateRect(){
        if (rectAnimator!=null && rectAnimator.isRunning()){
            rectAnimator.cancel();
        }
        rectAnimator = ValueAnimator.ofFloat(1.0f,0.0f);
        rectAnimator.setDuration(300);
        rectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        rectAnimator.addUpdateListener(animation -> {
            bgRectRate = (float) animation.getAnimatedValue();
            Log.i("zml","value="+bgColor);
            invalidateSelf();
        });
        rectAnimator.start();
    }

    private void animateDot(){
        if (dotAnimator!=null && dotAnimator.isRunning()){
            dotAnimator.cancel();
        }
        dotAnimator = ValueAnimator.ofFloat(1.0f,0.0f);
        dotAnimator.setDuration(500);
        dotAnimator.setRepeatCount(INFINITE);
        dotAnimator.setRepeatMode(REVERSE);
        dotAnimator.setInterpolator(new FastOutLinearInInterpolator());

        dotAnimator.addUpdateListener(animation -> {
            dotRate = (float) animation.getAnimatedValue();
            invalidateSelf();
        });
        dotAnimator.start();
    }


    public void setStatus(int status){
        this.status = status;
        invalidateSelf();
    }


    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        super.onBoundsChange(bounds);
        resourceRectF.set(bounds.left,bounds.top,bounds.right,bounds.bottom);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {


        float radius =  resourceRectF.height()/2;
        float left = resourceRectF.width()/2-radius;
        float right = resourceRectF.width()/2+radius;

        paint.setColor(Color.TRANSPARENT);
        canvas.drawRoundRect(resourceRectF,0,0,paint);

        paint.setColor(bgColor);
//        rectF.set(left*(1.0f-bgRectRate),resourceRectF.top,
//                resourceRectF.right-(left*(1.0f-bgRectRate)),resourceRectF.bottom);
//        canvas.drawRoundRect(rectF,resourceRectF.height()/2,resourceRectF.height()/2,paint);


        float space = (resourceRectF.width())/3;
        paint.setColor(Color.parseColor("#FFBB86FC"));
        for (int i = 0; i < 3; i++) {
            float _r = radius/4;
            if (i == 1){
                canvas.drawCircle(space*i+space/2,
                        resourceRectF.height()/2, _r, paint);
            }else if (i == 0){
                canvas.drawCircle(space*i+space/2+(1.0f-dotRate)*space,
                        resourceRectF.height()/2, _r, paint);
            }else if (i == 2){
                canvas.drawCircle(space*i+space/2-(1.0f-dotRate)*space,
                        resourceRectF.height()/2, _r, paint);
            }
        }


        int strokeW = DensityUtil.getPixels(6,contextWeakReference.get());
        paint.setStrokeWidth(strokeW);
        float cx = resourceRectF.width()/2;
        float cy = resourceRectF.height()/2;

        /*
        paint.setColor(Color.WHITE);
        canvas.drawCircle(cx,cy,radius,paint);

         */

        if (false){
            paint.setColor(Color.WHITE);
            paint.setStrokeCap(Paint.Cap.ROUND);
            float endX1 = cx-(radius/5*3*(1-rate));//cx
            float endY1 = cy+(rate*cy/2); //(cy+cy/2);
            canvas.drawLine(cx-radius/5*3,cy,endX1,endY1,paint);
           // Log.i("zml","rate="+rate);
            int[] mat = caclPoint((int) cy,60);
            canvas.drawLine(endX1,endY1,cx+mat[0]-strokeW*2,cy-mat[1]-strokeW,paint);
        }


        //paint.setStyle(Paint.Style.STROKE);

        //canvas.drawColor(bgColor);
    }

    private void tickAnimation(){
        if (rateAnimator!=null && rateAnimator.isRunning()){
            rateAnimator.cancel();
        }
        rateAnimator = ValueAnimator.ofFloat(0.0f,1.0f);
        rateAnimator.setDuration(300);
        rateAnimator.setInterpolator(new LinearInterpolator());

        rateAnimator.addUpdateListener(animation -> {
            rate = (float) animation.getAnimatedValue();
            invalidateSelf();
        });
        rateAnimator.start();
    }

    /**
     * 计算圆周上的某一点，
     * @param radius 半径
     * @param angle 角度
     * @return
     */
    private int[] caclPoint(int radius,int angle){
        //中心坐标
        //x = width / 2 ;//我这里是以屏幕中心为坐标点 所以宽高直接除以2
        // y = height / 2;//我这里是以屏幕中心为坐标点 所以宽高直接除以2
        //30 代表角度，200代表半径 这些都是可以自定义的
        //公式 java中有快捷求cos sin得方法。就是这样了

        int x = (int) Math.round(Math.sin(Math.toRadians(angle)) * radius);

        int y = (int) Math.round(Math.cos(Math.toRadians(angle)) * radius);
        return new int[]{x,y};
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return TRANSLUCENT;
    }
}
