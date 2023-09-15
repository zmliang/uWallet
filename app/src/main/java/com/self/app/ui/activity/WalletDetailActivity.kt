package com.self.app.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.ToastUtils
import com.self.app.R
import com.self.app.databinding.ActivityWalletDetailBinding
import com.self.app.encipherFormat
import com.self.app.eventViewModel
import com.self.app.viewmodel.WalletDetailViewModel
import com.zml.baseui.view.ExpendEditView
import com.zml.wallet.WalletInfo

class WalletDetailActivity:BaseLoadingActivity<WalletDetailViewModel,ActivityWalletDetailBinding>() {
    private var walletInfo:WalletInfo? = null

    override fun createDataObserver() {
        mViewModel.mnemonic.observeInActivity(this) {
            clip(it)
        }

        mViewModel.privateKey.observeInActivity(this) {
            clip(it)
        }

        mViewModel.isDeleted.observeInActivity(this){
            if (it){
                ToastUtils.showShort("删除钱包成功")
                eventViewModel.deleteWalletEvent.postValue(true)
                finish()
            }else{
                ToastUtils.showShort("删除钱包失败")
            }
        }
    }

    override fun getLayoutId(): Int {
       return R.layout.activity_wallet_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        intent?.let {
            walletInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableExtra("wallet_info",WalletInfo::class.java)
            }else{
                it.getParcelableExtra("wallet_info")
            }
            Log.i("zml","wallet=${walletInfo}")
        }
        walletInfo?.let {
            mViewDataBinding.walletAddressView.content = it.address?.encipherFormat()
            mViewDataBinding.walletNameView.content = it.walletName
            mViewDataBinding.walletPrivatekeyView.content ="********"
            mViewDataBinding.walletMnemonicView.content = "********"
            mViewDataBinding.walletPublickeyView.content = it.publicKey?.encipherFormat()
        }
        mViewDataBinding.apply {
            walletAddressView.setOnViewClickListener { id->
                when(id){
                    ExpendEditView.AREA_RIGHT_ICON ->{
                        copy(0)
                    }
                }
             }
            walletPrivatekeyView.setOnViewClickListener { id->
                when(id){
                    ExpendEditView.AREA_RIGHT_ICON ->{
                        copy(1)
                    }
                }
            }
            walletMnemonicView.setOnViewClickListener { id->
                when(id){
                    ExpendEditView.AREA_RIGHT_ICON ->{
                        copy(2)
                    }
                }
            }
            walletPublickeyView.setOnViewClickListener { id->
                when(id){
                    ExpendEditView.AREA_RIGHT_ICON ->{
                        copy(3)
                    }
                }
            }

            walletNameView.setOnViewClickListener {id->
                when(id){
                    ExpendEditView.AREA_RIGHT_ICON ->{
                        updateName(walletNameView.content)
                    }
                }

            }

            confirmTokenBtn.setOnClickListener {
                walletInfo?.let { it1 -> mViewModel.deleteWallet(it1) }
            }
        }


        mViewDataBinding.norBar.backAction.setOnClickListener { finish() }
    }

    fun updateName(name:String){

        walletInfo?.let {
            if (it.walletName == name){
                ToastUtils.showShort("名字没变")
                return
            }
            it.walletName = name
            mViewModel.updateName(it)
        }
    }

    private fun copy(type:Int){

        var content = ""
        when(type){
            0->{//地址
                content = walletInfo?.address?:""
            }
            1->{//私钥
                mViewModel.queryPrivateKey(walletInfo?.walletKey!!)
            }
            2->{//助记词
                mViewModel.queryMnemonic(walletInfo?.walletKey!!)
            }
            3->{//公钥
                content = walletInfo?.publicKey?:""
            }
        }
        if (content.isEmpty()){
            return
        }
       clip(content)
    }

    private fun clip(content:String){
        Log.i("zml","要复制到剪切板的内容=${content}")
        val manager: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        manager.setPrimaryClip(ClipData.newPlainText("encir",content))
        ToastUtils.showShort("已复制到剪切板")
    }

}