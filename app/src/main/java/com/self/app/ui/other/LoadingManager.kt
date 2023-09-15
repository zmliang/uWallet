package com.self.app.ui.other

import android.app.Activity
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.self.app.R
import com.self.app.ex.SettingUtil
import com.self.app.getPixels
import com.zml.baseui.view.ProgressButton


class LoadingManager private constructor() {

    companion object {
        val instance: LoadingManager by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LoadingManager()
        }
    }


    //loading框
    private var loadingDialog: MaterialDialog? = null

    /**
     * 打开等待框
     */
    fun show(context:AppCompatActivity, message: String = "请求网络中") {
        if (!context.isFinishing) {
            if (loadingDialog == null) {
                loadingDialog = MaterialDialog(context)
                    .cancelable(true)
                    .cancelOnTouchOutside(true)
                    .cornerRadius(5f)
                    .customView(R.layout.loading)
                    .lifecycleOwner(context)
                    .maxWidth(null,getPixels(80,context))
                    .onDismiss { loadingDialog = null }

                loadingDialog?.getCustomView()?.run {
                    //this.findViewById<TextView>(R.id.loading_tips).text = message
                }
            }

            loadingDialog?.apply {
                this.findViewById<ProgressButton>(R.id.progressBar).startProgress()
                this.show()
            }
        }
    }

    /**
     * 打开等待框
     */
    fun show(fragment: Fragment,message: String = "请求网络中") {
        fragment.activity?.let {
            if (!it.isFinishing) {
                if (loadingDialog == null) {
                    loadingDialog = MaterialDialog(it)
                        .cancelable(true)
                        .cancelOnTouchOutside(false)
                        .cornerRadius(12f)
                        .customView(R.layout.loading)
                        .lifecycleOwner(fragment)
                    loadingDialog?.getCustomView()?.run {
                        //this.findViewById<TextView>(R.id.loading_tips).text = message
                    }
                }
                loadingDialog?.run {
                    this.findViewById<ProgressButton>(R.id.progressBar).startProgress()
                    this.show()
                }
            }
        }
    }

    fun dismiss() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }


}
