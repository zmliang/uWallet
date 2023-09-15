package com.zml.baseui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Build
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.View.OnLongClickListener
import android.view.Window
import androidx.annotation.RequiresApi


class DialogController(mDialog: mDialog, window: Window) {
    //对话弹窗
    private val mEasyDialog: mDialog

    //窗口
    val window: Window

    //帮助类
    private var mViewHelper: DialogViewHelper? = null

    init {
        mEasyDialog = mDialog
        this.window = window
    }

    fun setDialogViewHelper(dialogViewHelper: DialogViewHelper?) {
        mViewHelper = dialogViewHelper
    }

    fun setText(viewId: Int, text: CharSequence?) {
        mViewHelper?.setText(viewId, text)
    }

    fun setTextColor(viewId: Int, color: Int) {
        mViewHelper?.setTextColor(viewId, color)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun setImageIcon(viewId: Int, icon: Icon?) {
        mViewHelper?.setImageIcon(viewId, icon)
    }

    fun setImageBitmap(viewId: Int, bitmap: Bitmap?) {
        mViewHelper?.setImageBitmap(viewId, bitmap)
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable?) {
        mViewHelper?.setImageDrawable(viewId, drawable)
    }

    fun setImageResource(viewId: Int, resId: Int) {
        mViewHelper?.setImageResource(viewId, resId)
    }

    fun getView(viewId: Int): View {
        return mViewHelper?.getSubView(viewId)!!
    }

    fun setBackgroundColor(viewId: Int, color: Int) {
        mViewHelper?.setBackgroundColor(viewId, color)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    fun setBackground(viewId: Int, drawable: Drawable?) {
        mViewHelper?.setBackground(viewId, drawable)
    }

    fun setOnClickListener(viewId: Int, onClickListener: View.OnClickListener?) {
        mViewHelper?.setOnClickListener(viewId, onClickListener)
    }

    val dialog: mDialog
        get() = mEasyDialog

    /**
     * 弹窗参数类
     */
    @SuppressLint("UseSparseArrays")
    class DialogParams(//上下文
        var mContext: Context, //主题资源Id
        var mThemeResId: Int
    ) {
        //点击其他区域弹窗是否取消
        var mCancelable = false

        //弹窗取消监听
        var mOnCancelListener: DialogInterface.OnCancelListener? = null

        //弹窗隐藏监听
        var mOnDismissListener: DialogInterface.OnDismissListener? = null

        //键值监听
        var mOnKeyListener: DialogInterface.OnKeyListener? = null

        //文本颜色
        var mTextColorArray = SparseArray<Int>()

        //背景颜色
        var mBgColorArray = SparseArray<Int>()

        //背景资源
        var mBgResArray = SparseArray<Drawable>()

        //存放文本
        var mTextArray = SparseArray<CharSequence>()

        //存放点击事件
        var mClickArray = SparseArray<View.OnClickListener>()

        //存放长按点击事件
        var mLongClickArray = SparseArray<OnLongClickListener>()

        //存放对话框图标
        var mIconArray = SparseArray<Icon>()

        //存放对话框图片
        var mBitmapArray = SparseArray<Bitmap>()

        //存放对话框图片
        var mDrawableArray = SparseArray<Drawable>()

        //存放对话框图标资源文件
        var mImageResArray = SparseArray<Int>()

        //对话框布局资源id
        var mLayoutResId = 0

        //对话框的内容view
        var mView: View? = null

        //对话框宽度
        var mWidth = 0

        //对话框高度
        var mHeight = 0

        //对话框垂直外边距
        var mHeightMargin = 0

        //对话框横向外边距
        var mWidthMargin = 0

        //对话框出现动画
        var mAnimation = 0

        //对话框显示位置，默认居中显示
        var mGravity = Gravity.CENTER

        /**
         * 应用
         */
        fun apply(controller: DialogController) {
            var helper: DialogViewHelper? = null
            if (mView != null) {
                helper = DialogViewHelper(mView!!)
            } else if (mLayoutResId != 0) {
                helper = DialogViewHelper(mContext, mLayoutResId)
            }

            //如果helper为null，则mLayoutResId为0，没有设置弹窗xml
            requireNotNull(helper) { "Please set layout!" }
            //为对话框设置内容视图
            controller.dialog.setContentView(helper.contentView)
            //设置DialogViewHelper辅助类
            controller.setDialogViewHelper(helper)
            //设置文本
            for (i in 0 until mTextArray.size()) {
                controller.setText(mTextArray.keyAt(i), mTextArray.valueAt(i))
            }
            //设置文本颜色
            for (i in 0 until mTextColorArray.size()) {
                controller.setTextColor(mTextColorArray.keyAt(i), mTextColorArray.valueAt(i))
            }
            //设置图标
            for (i in 0 until mIconArray.size()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    controller.setImageIcon(mIconArray.keyAt(i), mIconArray.valueAt(i))
                }
            }
            //设置图片
            for (i in 0 until mBitmapArray.size()) {
                controller.setImageBitmap(mBitmapArray.keyAt(i), mBitmapArray.valueAt(i))
            }
            //设置图片
            for (i in 0 until mDrawableArray.size()) {
                controller.setImageDrawable(mDrawableArray.keyAt(i), mDrawableArray.valueAt(i))
            }
            //设置图片
            for (i in 0 until mImageResArray.size()) {
                controller.setImageResource(mImageResArray.keyAt(i), mImageResArray.valueAt(i))
            }
            //设置背景颜色
            for (i in 0 until mBgColorArray.size()) {
                controller.setBackgroundColor(mBgColorArray.keyAt(i), mBgColorArray.valueAt(i))
            }
            //设置背景资源
            for (i in 0 until mBgResArray.size()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    controller.setBackground(mBgResArray.keyAt(i), mBgResArray.valueAt(i))
                }
            }
            //设置点击
            for (i in 0 until mClickArray.size()) {
                controller.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i))
            }
            //配置自定义效果，底部弹出，宽高，动画，全屏
            val window = controller.window
            //显示位置
            window.setGravity(mGravity)
            if (mAnimation != 0) {
                //设置动画
                window.setWindowAnimations(mAnimation)
            }
            //设置布局参数，主要是宽高和边距
            val params = window.attributes
            params.width = mWidth
            params.height = mHeight
            params.verticalMargin = mHeightMargin.toFloat()
            params.horizontalMargin = mWidthMargin.toFloat()
            window.attributes = params
        }
    }

    companion object{
        const val CONFIRM = 0
        const val CANCEL = 1
    }


    interface OnClickListener{
        fun on(type:Int)
    }
}