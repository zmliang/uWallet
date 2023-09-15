package com.zml.baseui.dialog



import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.view.Gravity
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import com.zml.baseui.R


class mDialog(context: Context, themeResId: Int) :
    Dialog(context, themeResId) {
    private val mController: DialogController

    init {
        mController = DialogController(this, window!!)
    }

    fun setText(viewId: Int, text: CharSequence?) {
        mController.setText(viewId, text)
    }

    fun getView(viewId: Int): View {
        return mController.getView(viewId)
    }

    fun setOnClickListener(viewId: Int, onClickListener: View.OnClickListener?) {
        mController.setOnClickListener(viewId, onClickListener)
    }

    /**
     * mDialog 构建器
     */
    class Builder @JvmOverloads constructor(
        context: Context?,
        themeResId: Int = R.style.MDialog
    ) {
        //弹窗参数类
        private val dialogParams: DialogController.DialogParams

        init {
            dialogParams = DialogController.DialogParams(context!!, themeResId)
            dialogParams.mGravity = Gravity.CENTER
            dialogParams.mAnimation = R.style.dialog_scale_anim
        }

        /**
         * 设置对话框内容视图
         *
         * @param view 视图
         * @return Builder
         */
        fun setContentView(view: View): Builder {
            dialogParams.mView = view
            dialogParams.mLayoutResId = 0
            return this
        }

        /**
         * 设置对话框内容视图
         *
         * @param layoutId 布局id
         * @return Builder
         */
        fun setContentView(layoutId: Int): Builder {
            dialogParams.mView = null
            dialogParams.mLayoutResId = layoutId
            return this
        }

        /**
         * 设置文本
         *
         * @param viewId 视图id
         * @param text   字符内容
         * @return Builder
         */
        fun setText(viewId: Int, text: CharSequence?): Builder {
            dialogParams.mTextArray.put(viewId, text)
            return this
        }

        /**
         * 设置文本颜色
         *
         * @param viewId 视图id
         * @param color  文字颜色
         * @return Builder
         */
        fun setTextColor(viewId: Int, color: Int): Builder {
            dialogParams.mTextColorArray.put(viewId, color)
            return this
        }

        /**
         * 设置背景
         *
         * @param viewId   视图id
         * @param drawable 资源
         * @return Builder
         */
        fun setBackground(viewId: Int, drawable: Drawable?): Builder {
            dialogParams.mBgResArray.put(viewId, drawable)
            return this
        }

        /**
         * 设置背景颜色
         *
         * @param viewId 视图id
         * @param color  颜色
         * @return Builder
         */
        fun setBackgroundColor(viewId: Int, color: Int): Builder {
            dialogParams.mBgColorArray.put(viewId, color)
            return this
        }

        /**
         * 设置图标
         *
         * @param viewId 视图id
         * @return Builder
         */
        fun setIcon(viewId: Int, icon: Icon?): Builder {
            dialogParams.mIconArray.put(viewId, icon)
            return this
        }

        /**
         * 设置图片
         *
         * @param viewId 视图id
         * @param bitmap 位图
         * @return Builder
         */
        fun setBitmap(viewId: Int, bitmap: Bitmap?): Builder {
            dialogParams.mBitmapArray.put(viewId, bitmap)
            return this
        }

        /**
         * 设置图片
         *
         * @param viewId   视图id
         * @param drawable 图片
         * @return Builder
         */
        fun setDrawable(viewId: Int, drawable: Drawable?): Builder {
            dialogParams.mDrawableArray.put(viewId, drawable)
            return this
        }

        /**
         * 设置图片
         *
         * @param viewId 视图id
         * @param resId  图片资源id
         * @return Builder
         */
        fun setImageRes(viewId: Int, resId: Int): Builder {
            dialogParams.mImageResArray.put(viewId, resId)
            return this
        }

        /**
         * 设置对话框宽度占满屏幕
         *
         * @return Builder
         */
        fun fullWidth(): Builder {
            dialogParams.mWidth = ViewGroup.LayoutParams.MATCH_PARENT
            return this
        }

        /**
         * 设置对话框高度占满屏幕
         *
         * @return Builder
         */
        fun fullHeight(): Builder {
            dialogParams.mHeight = ViewGroup.LayoutParams.MATCH_PARENT
            return this
        }

        /**
         * 设置对话框宽高
         *
         * @param width  宽
         * @param height 高
         * @return Builder
         */
        fun setWidthAndHeight(width: Int, height: Int): Builder {
            dialogParams.mWidth = width
            dialogParams.mHeight = height
            return this
        }

        /**
         * 设置对话框宽高和宽高边距
         *
         * @param width        宽
         * @param height       高
         * @param widthMargin  宽边距
         * @param heightMargin 高边距
         * @return Builder
         */
        fun setWidthAndHeightMargin(
            width: Int,
            height: Int,
            widthMargin: Int,
            heightMargin: Int
        ): Builder {
            dialogParams.mWidth = width
            dialogParams.mHeight = height
            dialogParams.mWidthMargin = widthMargin
            dialogParams.mHeightMargin = heightMargin
            return this
        }

        /**
         * 设置动画
         *
         * @param styleAnimation 对话框动画
         * @return Builder
         */
        fun setAnimation(styleAnimation: Int): Builder {
            dialogParams.mAnimation = styleAnimation
            return this
        }

        /**
         * 添加默认动画
         *
         * @return Builder
         */
        fun addDefaultAnimation(): Builder {
            dialogParams.mAnimation = R.style.dialog_scale_anim
            return this
        }


        /**
         * 设置对话框是否可取消，默认为true。
         *
         * @return Builder
         */
        fun setCancelable(cancelable: Boolean): Builder {
            dialogParams.mCancelable = cancelable
            return this
        }

        /**
         * 设置点击事件监听
         *
         * @param viewId          视图id
         * @param onClickListener 点击事件
         * @return Builder
         */
        fun setOnClickListener(viewId: Int, onClickListener: View.OnClickListener?): Builder {
            dialogParams.mClickArray.put(viewId, onClickListener)
            return this
        }

        /**
         * 设置长按事件监听
         *
         * @param viewId              视图id
         * @param onLongClickListener 长按事件
         * @return Builder
         */
        fun setOnLongClickListener(
            viewId: Int,
            onLongClickListener: OnLongClickListener?
        ): Builder {
            dialogParams.mLongClickArray.put(viewId, onLongClickListener)
            return this
        }

        /**
         * 设置取消事件监听
         *
         * @param onCancelListener 取消事件监听
         * @return Builder
         */
        fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): Builder {
            dialogParams.mOnCancelListener = onCancelListener
            return this
        }

        /**
         * 设置隐藏事件监听
         *
         * @param onDismissListener 隐藏事件监听
         * @return Builder
         */
        fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): Builder {
            dialogParams.mOnDismissListener = onDismissListener
            return this
        }

        /**
         * 设置键监听
         *
         * @param onKeyListener 键监听事件
         * @return Builder
         */
        fun setOnKeyListener(onKeyListener: DialogInterface.OnKeyListener): Builder {
            dialogParams.mOnKeyListener = onKeyListener
            return this
        }

        /**
         * 创建弹窗
         *
         * @return mDialog
         */
        fun create(): mDialog {
            //通过上下文和主题样式设置弹窗
            val dialog = mDialog(dialogParams.mContext, dialogParams.mThemeResId)
            //应用弹窗控制器
            dialogParams.apply(dialog.mController)
            //设置对话框是否可取消
            dialog.setCancelable(dialogParams.mCancelable)
            if (dialogParams.mCancelable) {
                //设置取消在触摸外面
                dialog.setCanceledOnTouchOutside(true)
            }
            dialog.setOnCancelListener(dialogParams.mOnCancelListener)
            dialog.setOnDismissListener(dialogParams.mOnDismissListener)
            if (dialogParams.mOnKeyListener != null) {
                dialog.setOnKeyListener(dialogParams.mOnKeyListener)
            }
            return dialog
        }

        /**
         * 显示弹窗
         *
         * @return dialog
         */
        fun show(): mDialog {
            val dialog = create()
            dialog.show()
            return dialog
        }
    }
}