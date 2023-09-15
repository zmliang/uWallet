package com.zml.baseui.dialog

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Build
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.lang.ref.WeakReference



class DialogViewHelper {
    /**
     * 获取弹窗内容视图
     *
     * @return mView
     */
    //内容视图
    val contentView: View

    //弱应用视图
    private val mSubViews: SparseArray<WeakReference<View>>

    /**
     * 构造方法
     *
     * @param view 视图
     */
    constructor(view: View) {
        //初始化
        mSubViews = SparseArray()
        //获取弹窗视图
        contentView = view
    }

    /**
     * 构造方法
     *
     * @param context     上下文
     * @param layoutResId 布局资源id
     */
    constructor(context: Context?, layoutResId: Int) {
        //初始化
        mSubViews = SparseArray()
        //获取弹窗视图
        contentView = LayoutInflater.from(context).inflate(layoutResId, null)
    }

    /**
     * 获取子视图
     *
     * @param viewId 视图id
     * @return view
     */
    fun getSubView(viewId: Int): View? {
        //通过视图id得到弱引用视图
        val viewWeakReference = mSubViews[viewId]
        var view: View? = null
        //如果弱引用视图不为空，说明有对应的xml文件，则对view进行赋值
        if (viewWeakReference != null) {
            view = viewWeakReference.get()
        }
        //如果view为空，则说明上面的弱引用列表数据不存在，通过findViewById方式重新赋值，不为空再放到数组里
        if (view == null) {
            view = contentView.findViewById(viewId)
            if (view != null) {
                mSubViews.put(viewId, WeakReference(view))
            }
        }
        return view
    }

    /**
     * 设置文本
     *
     * @param viewId 视图id
     * @param text   字符
     */
    fun setText(viewId: Int, text: CharSequence?) {
        val tv = getSubView(viewId) as TextView?
        if (tv != null) {
            tv.text = text
        }
    }

    /**
     * 设置文本
     *
     * @param viewId 视图id
     * @param color  颜色
     */
    fun setTextColor(viewId: Int, color: Int) {
        val tv = getSubView(viewId) as TextView?
        tv?.setTextColor(color)
    }

    /**
     * 设置图标
     *
     * @param viewId 视图id
     * @param icon   图标
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun setImageIcon(viewId: Int, icon: Icon?) {
        val iv = getSubView(viewId) as ImageView?
        iv?.setImageIcon(icon)
    }

    /**
     * 设置图片
     *
     * @param viewId 视图id
     * @param resId  图标资源id
     */
    fun setImageResource(viewId: Int, resId: Int) {
        val iv = getSubView(viewId) as ImageView?
        iv?.setImageResource(resId)
    }

    /**
     * 设置图片
     *
     * @param viewId   视图id
     * @param drawable 图
     */
    fun setImageDrawable(viewId: Int, drawable: Drawable?) {
        val iv = getSubView(viewId) as ImageView?
        iv?.setImageDrawable(drawable)
    }

    /**
     * 设置图片
     *
     * @param viewId 视图id
     * @param bitmap 图片
     */
    fun setImageBitmap(viewId: Int, bitmap: Bitmap?) {
        val iv = getSubView(viewId) as ImageView?
        iv?.setImageBitmap(bitmap)
    }

    /**
     * 设置背景颜色
     *
     * @param viewId 视图id
     * @param color  颜色
     */
    fun setBackgroundColor(viewId: Int, color: Int) {
        val view = getSubView(viewId)
        view?.setBackgroundColor(color)
    }

    /**
     * 设置背景
     *
     * @param viewId   视图id
     * @param drawable drawable
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    fun setBackground(viewId: Int, drawable: Drawable?) {
        val view = getSubView(viewId)
        if (view != null) {
            view.background = drawable
        }
    }

    /**
     * 设置点击监听
     *
     * @param viewId          视图id
     * @param onClickListener 点击监听
     */
    fun setOnClickListener(viewId: Int, onClickListener: View.OnClickListener?) {
        val view = getSubView(viewId)
        view?.setOnClickListener(onClickListener)
    }
}