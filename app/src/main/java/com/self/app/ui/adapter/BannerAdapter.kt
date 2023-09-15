package com.self.app.ui.adapter

import android.view.View
import com.self.app.R
import com.self.app.ui.banner.BannerViewHolder
import com.self.app.viewmodel.bean.BannerResponse
import com.zhpan.bannerview.BaseBannerAdapter

class BannerAdapter : BaseBannerAdapter<BannerResponse, BannerViewHolder>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.banner_itemhome
    }

    override fun createViewHolder(itemView: View, viewType: Int): BannerViewHolder {
        return BannerViewHolder(itemView);
    }

    override fun onBind(
        holder: BannerViewHolder?,
        data: BannerResponse?,
        position: Int,
        pageSize: Int
    ) {
        holder?.bindData(data, position, pageSize);
    }


}