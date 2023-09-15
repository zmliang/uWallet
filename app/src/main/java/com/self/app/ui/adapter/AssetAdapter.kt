package com.self.app.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.self.app.R
import com.self.app.isHeader
import com.self.app.ui.activity.AddTokenActivity
import com.self.app.ui.drawable.RoundedCornerBgDrawable
import com.self.app.ui.other.StickyHeaderHandler
import com.zml.wallet.TOKEN_HEADER
import com.zml.wallet.TokenInfo

class AssetAdapter(data: MutableList<TokenInfo>?) :
    BaseDelegateMultiAdapter<TokenInfo, BaseViewHolder>(data),
StickyHeaderHandler{

    private var totalBalance:Float = 0.0f

    enum class ItemType(val type:Int){
        HEADER(0),
        NORMAL(1)
    }

    private val animationMode = true


    init {

        this.setHasStableIds(true)
        this.data.add(0, TOKEN_HEADER)

        //是否开启item动画效果
        if (!animationMode) {
            this.animationEnable = false
        } else {
            this.animationEnable = true
            this.setAnimationWithDefault(AnimationType.values()[0])
        }
        // 第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<TokenInfo>() {
            override fun getItemType(data: List<TokenInfo>, position: Int): Int {
                return if (data[position].isHeader) {
                    ItemType.HEADER.type
                }else{
                    ItemType.NORMAL.type
                }
            }
        })
        // 第二步，绑定 item 类型
        getMultiTypeDelegate()?.let {
            it.addItemType(ItemType.HEADER.type, R.layout.item_asset_header)
            it.addItemType(ItemType.NORMAL.type, R.layout.item_asset_info)
        }
    }


    override fun convert(helper: BaseViewHolder, item: TokenInfo) {
        when (helper.itemViewType) {
            ItemType.HEADER.type -> {
                item.run {
                    helper.getView<TextView>(R.id.asset_header_add_coin).apply {
                        background = RoundedCornerBgDrawable(context.getDrawable(R.drawable.black_bg)!!)
                        setOnClickListener {
                            context.startActivity(Intent(context, AddTokenActivity::class.java))
                        }
                    }
                }
            }
            else -> {
                item.run {
                    helper.setText(R.id.asset_coin_symbol,item.symbol)
                    helper.setText(R.id.asset_total_balance,"$${item.balance}")
//                    helper.setText(R.id.asset_coin_price,item.curPrice)
//                    helper.setText(R.id.asset_coin_u_price,item.balance)
//                    helper.setText(R.id.asset_coin_volume,item.range.toString())
                }
            }
        }

    }


    override fun getHeaderPosition(itemPosition: Int): Int {
        var headerPosition = StickyHeaderHandler.HEADER_POSITION_NOT_FOUND
        var targetItemPosition = itemPosition
        do {
            if (isHeader(targetItemPosition)) {
                headerPosition = targetItemPosition
                headerPosition
                break
            }
            targetItemPosition -= 1
        } while (targetItemPosition >= 0)
        return headerPosition
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return R.layout.item_asset_header
    }

    override fun bindHeaderData(header: View?, headerPosition: Int) {
        header?:return

        val headerItem = data[headerPosition]
        if (headerItem.isHeader) {
            header.findViewById<TextView>(R.id.asset_header_add_coin)
                .background = RoundedCornerBgDrawable(context.getDrawable(R.drawable.black_bg)!!)
//            header.findViewById<TextView>(R.id.coin_name_title).text = "币种"
//            header.findViewById<TextView>(R.id.coin_price_title).text = "最新价"
//            header.findViewById<TextView>(R.id.coin_range_title).text = "今日涨跌"

        }

    }

    override fun isHeader(itemPosition: Int): Boolean {
        if (itemPosition>=data.size){
            return false
        }
        val item = data[itemPosition]
        return item.isHeader
    }



}