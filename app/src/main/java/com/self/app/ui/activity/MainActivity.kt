package com.self.app.ui.activity

import android.os.Bundle
import com.self.app.R
import com.self.app.appViewModel
import com.self.app.databinding.ActivityMainBinding
import com.self.app.ex.SettingUtil
import com.self.app.ex.StatusBarUtil
import com.self.app.viewmodel.MainViewModel
import com.self.base.ui.BaseActivity
import com.zml.wallet.WalletAccountMgr
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override fun createDataObserver() {
        //StatusBarUtil.setLightMode(this)
        StatusBarUtil.setColorNoTranslucent(this,SettingUtil.getColor(this,R.color.white))

//
//        supportActionBar?.displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
//        supportActionBar?.customView = HomeActionBarBinding.inflate(layoutInflater).root
//        supportActionBar?.setBackgroundDrawable(ColorDrawable(SettingUtil.getColor(this,R.color.white)))
////

//        appViewModel.appColor.observeInActivity(this, Observer {
//            Log.i("zml","回调")
//            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
//            StatusBarUtil.setColor(this, SettingUtil.getColor(this,R.color.white), 0)
//        })
    }

    override fun showLoading(message: String) {

    }

    override fun dismissLoading() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView(savedInstanceState: Bundle?) {

        val TT_CONTRACT = "0x17a011150e9Feb7bEc4CfAda055c8Df436EB730B"
        val wallet_address = "0x5D482E16A2c92d92841eaA2Cd4abcA76948d779F"

        GlobalScope.launch {
            WalletAccountMgr.get().addCustomCoin(TT_CONTRACT,wallet_address, appViewModel.curChain)
        }
    }

}

