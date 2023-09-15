package com.self.app.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.ToastUtils
import com.self.app.R
import com.self.app.databinding.ActivityTransferBinding
import com.self.app.isNumber
import com.self.app.ui.dialog.DialogManager
import com.self.app.viewmodel.TransferViewModel
import com.zml.baseui.view.ExpendEditView
import com.zml.wallet.TokenInfo

class TransferActivity:BaseLoadingActivity<TransferViewModel,ActivityTransferBinding>() {
    private lateinit var tokenInfo: ArrayList<TokenInfo>
    private var selectedPos = 0

    override fun createDataObserver() {

    }


    override fun getLayoutId(): Int {
       return R.layout.activity_transfer
    }

    override fun initView(savedInstanceState: Bundle?) {
        val tmp = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("all_token", TokenInfo::class.java)
        }else{
            intent.getParcelableArrayListExtra("all_token")
        })

        tmp?.let {
            tokenInfo = it
        }

        val curSeleToken = if (tokenInfo.size<=0)null else tokenInfo[selectedPos]

        mViewDataBinding.transAmount.apply {
            subTitle = "全部: $${curSeleToken?.balance}"
        }

        mViewDataBinding.transferTokenInfo.apply {
            content = curSeleToken?.symbol

            setOnViewClickListener { id->
                run {
                    when (id) {
                        ExpendEditView.AREA_CONTENT -> {
                            tokenInfo?.let {
                                DialogManager.instance.showTokens(this@TransferActivity, tokenInfo){
                                    Log.i("zml","选择了--》$it")
                                    notifySelectedTokenChanged(it)
                                }
                            }
                        }
                    }
                }
            }
        }

        mViewDataBinding.norBarTrans.backAction.setOnClickListener { finish() }

        mViewDataBinding.transConfirmBtn.setOnClickListener {

            val address = mViewDataBinding.transToAddress.content
            if (address.isNullOrEmpty()){
                ToastUtils.showShort("请输入有效收款地址")
                return@setOnClickListener
            }
            val amount = mViewDataBinding.transAmount.content
            if (!amount.isNumber()){
                ToastUtils.showShort("请输入有效数字")
                return@setOnClickListener
            }
            val curToken = tokenInfo[selectedPos]
            if (amount.toFloat() > (curToken.balance?.toFloat() ?: 0.0f)){
                ToastUtils.showShort("余额不足")
                return@setOnClickListener
            }
            mViewModel.showLoading.postValue("请求中")
            mViewModel.transfer(address,curToken.contract,amount)


        }

        mViewDataBinding.transToAddress.apply {
            setOnViewClickListener {id->
                run {
                    when (id) {
                        ExpendEditView.AREA_RIGHT_ICON -> {
                            startActivityForResult(
                                Intent(
                                    this@TransferActivity,
                                    CameraPreviewActivity::class.java
                                ), 0
                            )
                        }
                    }
                }
            }
        }
    }

    fun notifySelectedTokenChanged(index:Int){
        if (selectedPos == index){
            return
        }
        selectedPos = index
        val curToken = tokenInfo[selectedPos]
        mViewDataBinding.transferTokenInfo.content = curToken.symbol
        mViewDataBinding.transAmount.subTitle = "全部:$${curToken.balance}"
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        if (0 == requestCode && 0 == resultCode) {
            val s = data.getStringExtra("content")
            mViewDataBinding.transToAddress.content = s
        }
    }

}