package com.self.app.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Method;

public class RefreshLayout extends LinearLayout {
    private int mLastX = 0;
    private int mLastY = 0;

    public RefreshLayout(Context context) {
        super(context);
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:{
                Log.i("zml","child dispatch down");
                getParent().requestDisallowInterceptTouchEvent(true);//先不允许父容器拦截
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if(((ViewGroup)getParent()).getScrollY()<200){
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                break;
            }
            default:
                break;
        }
        mLastY = y;
        mLastX = x;
        return super.dispatchTouchEvent(ev);//这是为了不影响ListView原本的功能
    }




}
