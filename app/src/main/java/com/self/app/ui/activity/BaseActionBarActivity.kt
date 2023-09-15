package com.self.app.ui.activity

import android.os.Bundle
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.self.app.R
import com.self.app.ex.SettingUtil
import com.self.app.ex.StatusBarUtil
import com.self.base.BaseViewModel
import com.self.base.ui.BaseActivity

abstract class BaseActionBarActivity<VM: BaseViewModel, VB: ViewBinding> : BaseActivity<VM, VB>() {
    private lateinit var titleTv:TextView

    override fun initView(savedInstanceState: Bundle?) {
        StatusBarUtil.setColorNoTranslucent(this,SettingUtil.getColor(this,R.color.white))
    }

    open fun setTitle(title:String="Default Title"){
        titleTv.text=title
    }

}

