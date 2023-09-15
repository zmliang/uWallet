package com.self.app.ui.adapter

import android.graphics.Paint
import android.util.Log
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.self.app.R
import com.self.app.appViewModel
import com.self.app.encipherFormat
import com.self.app.getPixels
import com.self.app.ui.drawable.RoundedCornerBgDrawable
import com.self.app.viewmodel.bean.TokenTransEvent


class TokenTransEventAdapter(data: MutableList<TokenTransEvent>?) :
    BaseDelegateMultiAdapter<TokenTransEvent, BaseViewHolder>(data){

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
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<TokenTransEvent>() {
            override fun getItemType(data: List<TokenTransEvent>, position: Int): Int {
                return 0
            }
        })

        getMultiTypeDelegate()?.addItemType(0, R.layout.item_token_trans_event)
    }

    override fun convert(helper: BaseViewHolder, item: TokenTransEvent) {
        val color = context.getColor(if (item.isIncome)R.color.green else R.color.red_normal)
        helper.getView<TextView>(R.id.title_trans_type).apply {
            val drawable= AppCompatResources.getDrawable(context,if (item.isIncome)R.drawable.receive_icon
            else R.drawable.send_icon)
            drawable?.setBounds(0,0, getPixels(20,context),
                getPixels(20,context)
            )
            this.setCompoundDrawables(drawable,null,null,null)
        }
        helper.setText(R.id.value_trans_type,"${if (item.isIncome) '+' else '-'}"+item.value+" ${item.tokenSymbol}")
        helper.setTextColor(R.id.value_trans_type,color)
        helper.setText(R.id.value_collect_addr,item.to.encipherFormat())
        helper.setText(R.id.value_payment_addr,item.from.encipherFormat())
        helper.setText(R.id.value_trans_hash,item.hash.encipherFormat())
        helper.getView<TextView>(R.id.value_trans_hash).paint.flags = Paint.UNDERLINE_TEXT_FLAG
        helper.setText(R.id.value_gas_fee,item.gas+" ${appViewModel.curChain.symbol}")
        helper.setText(R.id.value_trans_time,item.timeStamp)
    }



}