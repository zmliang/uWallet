package com.self.app.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.self.app.R
import com.self.app.databinding.ActivityTokenTransEventBinding
import com.self.app.ui.adapter.TokenTransEventAdapter
import com.self.app.viewmodel.TokenTransEventViewModel
import com.zml.wallet.TokenInfo


class TokenTransEventActivity:BaseLoadingActivity<TokenTransEventViewModel,ActivityTokenTransEventBinding>() {

    private val mAdapter =TokenTransEventAdapter(arrayListOf())

    override fun createDataObserver() {
        mViewModel.tokenTrans.observe(this){
            mViewDataBinding.meRefreshLayout.finishRefresh()
            mViewDataBinding.meRefreshLayout.finishLoadMore()
            if (!it.hasMore){
                ToastUtils.showShort("没有更多了")
                return@observe
            }
            if (it.isRefresh){
                mAdapter.setList(it.listData)
            }else{
                mAdapter.addData(it.listData)
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_token_trans_event
    }

    override fun initView(savedInstanceState: Bundle?) {

        mViewDataBinding.meRefreshLayout.run {
            setEnableLoadMore(true)//是否启用上拉加载功能
            setEnableOverScrollBounce(true)//是否启用越界回弹
            setEnableOverScrollDrag(true)//是否启用越界拖动

            setOnRefreshListener {
                mViewModel.requestTokenTrans(true)
            }

            setOnLoadMoreListener {
                mViewModel.requestTokenTrans(false)
            }
        }

        mViewDataBinding.recyclerView.run {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
            isNestedScrollingEnabled=true

        }

        mViewDataBinding.norBar.backAction.apply {
            setOnClickListener {
                finish()
            }
        }

        val token =if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("token", TokenInfo::class.java)
        }else{
            intent.getParcelableExtra("token")
        }
        if (token != null) {
            mViewModel.curToken = token
            mViewModel.requestTokenTrans(true)
        }

    }
}