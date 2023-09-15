package com.self.app.ui.dialog

import android.content.Context
import android.widget.ImageView
import com.self.app.R
import com.zml.baseui.DensityUtil
import com.zml.baseui.dialog.mDialog
import com.zml.zbar.ZxingUtil

object QRCodeDialog {

    fun show(content:String,context:Context){
        //通过构建者模式设置弹窗的相关参数
        val builder: mDialog.Builder = mDialog.Builder(context)
            .addDefaultAnimation()
            .setContentView(R.layout.dialog_receive_qrcode)
            .setWidthAndHeight(
                DensityUtil.getPixels(280, context),
                DensityUtil.getPixels(320, context)
        )
            .setCancelable(true) //设置标题
        val dialog = builder.create()
        (dialog.getView(R.id.qrcode_iv) as ImageView ).setImageBitmap(ZxingUtil.createImage(content))
        dialog.show()
    }

}