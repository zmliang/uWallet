package com.self.app.ui.adapter

import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.self.app.R
import com.self.app.ui.other.StickyHeaderHandler
import com.self.app.viewmodel.bean.MatchingOrderInfo


class HandicapAdapter(data: MutableList<MatchingOrderInfo>?) :
    BaseDelegateMultiAdapter<MatchingOrderInfo, BaseViewHolder>(data),
StickyHeaderHandler{

    enum class ItemType(val type:Int){
        HEADER(0),
        NORMAL(1)
    }

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
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<MatchingOrderInfo>() {
            override fun getItemType(data: List<MatchingOrderInfo>, position: Int): Int {
                return if (data[position].isHeader) {
                    ItemType.HEADER.type
                }else{
                    ItemType.NORMAL.type
                }

            }
        })
        // 第二步，绑定 item 类型
        getMultiTypeDelegate()?.let {
            it.addItemType(ItemType.HEADER.type, R.layout.item_header)
            it.addItemType(ItemType.NORMAL.type, R.layout.item_token_info)
        }
    }

    override fun convert(helper: BaseViewHolder, item: MatchingOrderInfo) {
        when (helper.itemViewType) {
            ItemType.HEADER.type -> {
                item.run {

//                    helper.setText(R.id.coin_name_title, "币种")
//                    helper.setText(R.id.coin_range_title, "今日涨跌")
//                    helper.setText(R.id.coin_price_title,"最新价")

                }
            }
            else -> {
                item.run {

//                    helper.setText(R.id.coin_name, if (coinName.isNotEmpty()) coinName else "Unknown")
//                    helper.setText(R.id.coin_range, "$range%")
//                    helper.setText(R.id.coin_price, if (curPrice.isNotEmpty()) curPrice else "0.0")
//                    Glide.with(context).load(coinIcon)
//                        .transition(DrawableTransitionOptions.withCrossFade(500))
//                        .into(helper.getView(R.id.coin_icon))

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
        return R.layout.item_header
    }

    override fun bindHeaderData(header: View?, headerPosition: Int) {
        header?:return

        val headerItem = data[headerPosition]
        if (headerItem.isHeader) {

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