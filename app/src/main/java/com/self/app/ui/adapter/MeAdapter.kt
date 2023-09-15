package com.self.app.ui.adapter

import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.self.app.R
import com.self.app.viewmodel.bean.PageMeInfo

class MeAdapter(data: MutableList<PageMeInfo>?) :
    BaseDelegateMultiAdapter<PageMeInfo, BaseViewHolder>(data){


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
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<PageMeInfo>() {
            override fun getItemType(data: List<PageMeInfo>, position: Int): Int {
                return 0

            }
        })
        // 第二步，绑定 item 类型
        getMultiTypeDelegate()?.let {
            it.addItemType(0, R.layout.page_me_item)
        }
    }

    override fun convert(helper: BaseViewHolder, item: PageMeInfo) {
        helper.setText(R.id.me_action_name,item.name)

    }
}