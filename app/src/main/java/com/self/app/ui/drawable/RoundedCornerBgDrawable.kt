package com.self.app.ui.drawable

import android.graphics.*
import android.graphics.PixelFormat.*
import android.graphics.drawable.Drawable

/**
 * 圆角的drawable
 */
class RoundedCornerBgDrawable constructor(val mDrawable: Drawable,val color:Int = Color.BLACK,
val stroke:Boolean=false) : Drawable(){
    private val mPaint = Paint()

    init {
        mPaint.style = if (stroke) Paint.Style.STROKE else Paint.Style.FILL
        mPaint.color = color
        bounds = Rect(mDrawable.bounds)
        mPaint.isAntiAlias = true
    }

    override fun draw(canvas: Canvas) {
        val rect = bounds
        val r:Float = (bounds.height()/2).toFloat()

        canvas.drawRoundRect(RectF(rect.left.toFloat(),rect.top.toFloat(),rect.right.toFloat(),rect.bottom.toFloat()),
            r, r, mPaint)
    }

    //透明度
    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    //颜色过滤器
    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    // 必须重写的方法，返回此Drawable的不透明度/透明度
    //返回值必须是如下几个值：
    //PixelFormat.UNKNOWN
    //PixelFormat.TRANSLUCENT 只有绘制的地方才覆盖底下的内容
    //PixelFormat.TRANSPARENT 透明，完全不显示任何东西
    //PixelFormat.OPAQUE 完全不透明，遮盖在它下面的所有内容
    //PixelFormat.OPAQUE
    override fun getOpacity(): Int {
        return TRANSPARENT
    }

    override fun getIntrinsicHeight(): Int {
        return mDrawable.intrinsicHeight
    }

    override fun getIntrinsicWidth(): Int {
        return mDrawable.intrinsicWidth
    }
}