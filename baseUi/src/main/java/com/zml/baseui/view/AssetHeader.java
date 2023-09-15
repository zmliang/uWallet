package com.zml.baseui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class AssetHeader extends androidx.appcompat.widget.AppCompatTextView {
    private String balance = "";

    public AssetHeader(@NonNull Context context) {
        super(context);
    }

    public AssetHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AssetHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       
    }

    public void setBalance(String balance){
        TextPaint textPaint = getPaint();
        float textWidth = textPaint.measureText(balance);
        Log.d("zml", "宽度==:"+textWidth+"： view宽="+this.getWidth());
        float maxWidth = getWidth();
        float textSize = textPaint.getTextSize();
//        while (textWidth>maxWidth){
//            textSize-=1;
//            textPaint.setTextSize(textSize);
//            textWidth = getPaint().measureText(balance);
//        }
        setText(balance);
    }


}
