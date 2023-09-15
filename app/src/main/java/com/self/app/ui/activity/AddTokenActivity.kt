package com.self.app.ui.activity

import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.ToastUtils
import com.self.app.R
import com.self.app.databinding.ActivityAddTokenBinding
import com.self.app.eventViewModel
import com.self.app.viewmodel.AddTokenViewModel

class AddTokenActivity:BaseLoadingActivity<AddTokenViewModel,ActivityAddTokenBinding>() {
    override fun createDataObserver() {
        mViewModel.tokenInfo.observeInActivity(this){
            mViewModel.dismissLoading.postValue(true)
            mViewDataBinding.tokenSymbol.content = it.symbol
            mViewDataBinding.tokenDecimal.content = it.decimal

            if (!it.isSuccess){
                ToastUtils.showShort("添加币种失败")
            }else{
                if (!it.isFresh){
                    ToastUtils.showShort("已经添加过该币种")
                }else{
                    ToastUtils.showShort("添加成功，返回首页查看")
                    eventViewModel.addTokenEvent.postValue(it)
                }
            }
        }
    }


    override fun getLayoutId(): Int {
       return R.layout.activity_add_token
    }

    override fun initView(savedInstanceState: Bundle?) {

        mViewDataBinding.norBar.backAction.setOnClickListener { finish() }

        mViewDataBinding.addTokenBtn.setOnClickListener {
            mViewModel.showLoading.postValue("请求新代币")
            mViewModel.addToken(mViewDataBinding.tokenContractView.content)
        }

    }

}