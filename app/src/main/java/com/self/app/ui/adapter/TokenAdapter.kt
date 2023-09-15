package com.self.app.ui.adapter


import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.self.app.R
import com.zml.wallet.WalletInfo

class TokenAdapter(data: MutableList<WalletInfo>?) :
    BaseDelegateMultiAdapter<WalletInfo, BaseViewHolder>(data){


    private val animationMode = true


    init {
        //是否开启item动画效果
        if (!animationMode) {
            this.animationEnable = false
        } else {
            this.animationEnable = true
            this.setAnimationWithDefault(AnimationType.values()[0])
        }
        // 第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<WalletInfo>() {
            override fun getItemType(data: List<WalletInfo>, position: Int): Int {
                return 0

            }
        })
        // 第二步，绑定 item 类型
        getMultiTypeDelegate()?.let {
            it.addItemType(0, R.layout.token_card)
        }
    }

    override fun convert(helper: BaseViewHolder, item: WalletInfo) {
        //helper.setText(R.id.wallet_address, item.address)

    }



}