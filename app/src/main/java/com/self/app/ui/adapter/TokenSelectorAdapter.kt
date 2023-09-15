package com.self.app.ui.adapter

import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.self.app.R
import com.zml.wallet.TokenInfo

class TokenSelectorAdapter(data: MutableList<TokenInfo>?) :
    BaseDelegateMultiAdapter<TokenInfo, BaseViewHolder>(data){
    private val animationMode = true


    init {

        this.setHasStableIds(true)

        if (!animationMode) {
            this.animationEnable = false
        } else {
            this.animationEnable = true
            this.setAnimationWithDefault(AnimationType.values()[0])
        }
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<TokenInfo>() {
            override fun getItemType(data: List<TokenInfo>, position: Int): Int {
                return 0
            }
        })
        // 第二步，绑定 item 类型
        getMultiTypeDelegate()?.addItemType(0, R.layout.item_asset_info)
    }


    override fun convert(helper: BaseViewHolder, item: TokenInfo) {
        when (helper.itemViewType) {
            0 -> {
                item.run {
                    helper.setText(R.id.asset_coin_symbol,item.symbol)
                    helper.setText(R.id.asset_total_balance,"$${item.balance}")
                }
            }
        }

    }

}