package com.self.app.ui.adapter

import android.content.Intent
import android.widget.TextView
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.self.app.R
import com.self.app.encipherFormat
import com.self.app.ui.activity.AddWalletActivity
import com.self.app.ui.activity.WalletDetailActivity
import com.zml.wallet.WalletInfo
import com.zml.wallet.isPlaceholder

class WalletItemAdapter(data: MutableList<WalletInfo>?) :
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
                return if (data[position].isPlaceholder){
                     0
                }else{
                    1
                }
               
            }
        })
        // 第二步，绑定 item 类型
        getMultiTypeDelegate()?.addItemType(1, R.layout.wallet_card)
        getMultiTypeDelegate()?.addItemType(0, R.layout.wallet_card_placeholder)
    }

    override fun convert(helper: BaseViewHolder, item: WalletInfo) {
        when (helper.itemViewType){
            1->{
                helper.setText(R.id.card_wallet_name,item.walletName)
                helper.setText(R.id.card_wallet_address, item.address.encipherFormat())
                helper.getView<TextView>(R.id.card_wallet_item_detail).setOnClickListener{
                    context.startActivity(Intent(context,WalletDetailActivity::class.java)
                        .putExtra("wallet_info",item))
                }
            }
            0->{//没钱包时的占位
                helper.itemView.setOnClickListener {
                    context.startActivity(Intent(context,AddWalletActivity::class.java))
                }
            }
        }


    }



}