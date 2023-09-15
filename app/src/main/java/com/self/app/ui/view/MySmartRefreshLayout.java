package com.self.app.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.self.app.R;

public class MySmartRefreshLayout extends SmartRefreshLayout {

    private int mLastX;
    private int mLastY;

    private boolean isVerticalScrolling = false;

    private float startX;
    private float startY;
    private final String TAG = "MySmartRefreshLayout";
    public MySmartRefreshLayout(Context context) {
        super(context);
    }

    public MySmartRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                startX = ev.getX();
                isVerticalScrolling = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);

                if (!isVerticalScrolling) {
                    // If not already scrolling vertically, intercept the touch event
                    getParent().requestDisallowInterceptTouchEvent(true);
                    isVerticalScrolling = true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

   int headerHeight = 200;



    private RecyclerView childRv;

    public void setChildRv(RecyclerView childRv) {
        this.childRv = childRv;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (t >= headerHeight) {
            Log.i("zml","滑动里200像素");
            //this.childRv.setNestedScrollingEnabled(true);
        }
    }


//
//    @Override
//    public void scrollTo(int x, int y) {
//        int needY = y;
//        if (y<0){
//            needY = 0;
//        }
//        if (y>headerHeight){
//            needY = headerHeight;
//        }
//        super.scrollTo(x, needY);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        float y = ev.getY();
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                Log.i(TAG,"TOUCH ACTION_DOWN");
//                lastY = y;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.i(TAG,"TOUCH ACTION_MOVE");
//                float dy = lastY-y;
//                scrollBy(0, (int) dy);
//                lastY = y;
//                break;
//        }
//        return super.onTouchEvent(ev);
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent e) {
//        switch (e.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                Log.i(TAG,"DISPATCH ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.i(TAG,"DISPATCH ACTION_MOVE");
//                break;
//        }
//        return super.dispatchTouchEvent(e);
//    }
}
