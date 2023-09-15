package com.zml.baseui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.zml.baseui.DensityUtil.getPixels
import com.zml.baseui.R


object DialogUtil {

    private var tipDialog: mDialog? = null




    fun showDialog(
        context: Context?,
        title: String,
        content: String,
        clicked:DialogController.OnClickListener?
    ) {
        //通过构建者模式设置弹窗的相关参数
        val builder: mDialog.Builder = mDialog.Builder(context) //添加默认出现动画
            .addDefaultAnimation() //设置内容视图
            .setContentView(R.layout.dialog_wallet_info) //设置对话框可取消
            .setCancelable(true) //设置标题
 //           .setText(R.id.tv_title, title) //设置内容
            //.setText(R.id.tv_content, content) //设置文字颜色
//            .setTextColor(
//                R.id.tv_confirm,
//                ContextCompat.getColor(context!!, R.color.white)
//            ) //设置弹窗宽高
            .setWidthAndHeight(
                getPixels(280,context),
                LinearLayout.LayoutParams.WRAP_CONTENT
            )  //添加点击事件  确定
//            .setOnClickListener(R.id.tv_confirm) {
//                clicked?.on(DialogController.CONFIRM)
//                tipDialog?.dismiss()
//            }
        //创建弹窗
        tipDialog = builder.create()
        //显示弹窗
        tipDialog?.show()
    }

}