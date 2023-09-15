package com.self.app.ui.adapter

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.self.app.R
import com.self.app.ui.view.MySmartRefreshLayout
import com.self.app.viewmodel.bean.ChainInfo

class ChainAdapter(data: MutableList<ChainInfo>?) :
    BaseDelegateMultiAdapter<ChainInfo, BaseViewHolder>(data){

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
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<ChainInfo>() {
            override fun getItemType(data: List<ChainInfo>, position: Int): Int {
                return 0
            }
        })
        // 第二步，绑定 item 类型
        getMultiTypeDelegate()?.let {
            it.addItemType(0, R.layout.wallet_chain_layout)
        }
    }

    override fun convert(helper: BaseViewHolder, item: ChainInfo) {
        helper.setText(R.id.wallet_chain_name,item.name)
        val childRv = helper.getView<RecyclerView>(R.id.child_rv)
        Log.i("zml","item.walletInfos="+item.walletInfos.size)
        val _adapter =  TokenAdapter(item.walletInfos)
        childRv.run {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = _adapter
            //disNestedScrollingEnabled=true
            isNestedScrollingEnabled=false
            //(parent?.parent as MySmartRefreshLayout).setChildRv(this)
//            addOnScrollListener(object : OnScrollListener(){
//
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//                    if (!recyclerView.canScrollVertically(-1)) {
//                        parent.parent.requestDisallowInterceptTouchEvent(false);
//                    }
//                }
//            })
        }


    }



}