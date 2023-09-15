package com.self.app.ui.activity

import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.ToastUtils
import com.self.app.R
import com.self.app.appViewModel
import com.self.app.databinding.ActivityAddWalletBinding
import com.self.app.eventViewModel
import com.self.app.ex.CacheUtil
import com.self.app.viewmodel.AddWalletViewModel
import com.zml.baseui.DensityUtil
import com.zml.baseui.view.ExpendEditView.AREA_TITLE

class AddWalletActivity:BaseLoadingActivity<AddWalletViewModel,ActivityAddWalletBinding>() {
    private var newWalletType = 1;//创建新钱包，1：导入私钥 2：导入助记词

    override fun createDataObserver() {
        mViewModel.walletInfo.observeInActivity(this) {
            mViewModel.dismissLoading.postValue(true)
            Log.i("zml", "新钱包信息=${it}")
            if (it == null){
                ToastUtils.showShort("创建钱包失败")
            }else{
                if (!it.isFresh){
                    ToastUtils.showShort("该钱包已存在于${appViewModel.curChain.name} 链")
                }else{
                    eventViewModel.updateCurWalletEvent.postValue(it)
                    finish()
                }
            }
        }
    }

    override fun getLayoutId(): Int {
       return R.layout.activity_add_wallet
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewDataBinding.click = ClickListener()
        mViewDataBinding.norBar.backAction.setOnClickListener { finish() }

        mViewDataBinding.newWalletNameEe.hint = appViewModel.curChain.name+CacheUtil.autoGen(
            appViewModel.curChain.chainId?:"")

        mViewDataBinding.createWallletTv.setOnViewClickListener { id->
            when(id){
                AREA_TITLE->{
                    newWalletType=0
                }
            }
        }
        mViewDataBinding.privateKeyEev.setOnViewClickListener { id->
            when(id){
                AREA_TITLE->{
                    newWalletType = 1
                }
            }
        }
        mViewDataBinding.mnemonicEev.setOnViewClickListener { id->
            when(id){
                AREA_TITLE->{
                    newWalletType = 2
                }
            }
        }
    }

    private fun doCreate(content:String,walletName:String){
        mViewModel.showLoading.postValue("创建新钱包")
        mViewModel.createWallet(newWalletType,content,walletName)
    }

    inner class ClickListener{
        fun onClick() {
            var content = ""
            when(newWalletType){
                0->{
                    content = "new"
                }
                1->{
                    content = mViewDataBinding.privateKeyEev.content
                }
                2->{
                    content = mViewDataBinding.mnemonicEev.content
                }
            }
            if (content.isEmpty()){
                return
            }
            val name = if (mViewDataBinding.newWalletNameEe.content.isNullOrEmpty()){
                mViewDataBinding.newWalletNameEe.hint
            }else{
                mViewDataBinding.newWalletNameEe.content
            }
            doCreate(content,name)
        }
    }
}