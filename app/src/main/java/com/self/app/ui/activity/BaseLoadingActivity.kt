package com.self.app.ui.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.self.app.ex.SettingUtil
import com.self.app.ex.StatusBarUtil
import com.self.app.ui.other.LoadingManager
import com.self.base.BaseViewModel
import com.self.base.ui.BaseActivity

abstract class BaseLoadingActivity<VM: BaseViewModel, VB: ViewBinding>:BaseActivity<VM,VB>(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setColorNoTranslucent(this, SettingUtil.getColor(this,android.R.color.transparent))
    }


    override fun showLoading(message: String) {
        LoadingManager.instance.show(this,message)
    }

    override fun dismissLoading() {
        LoadingManager.instance.dismiss()
    }
}
