package com.zml.baseui;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class DensityUtil {
    public static int getPixels(int dipValue, Context context){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dipValue,
                context.getResources().getDisplayMetrics()
        );
    }


}
