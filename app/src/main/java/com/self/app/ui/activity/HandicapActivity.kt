

package com.self.app.ui.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.self.app.R
import com.self.app.databinding.FragmentHandicapBinding
import com.self.app.ex.SettingUtil
import com.self.app.ex.StatusBarUtil
import com.self.app.getPixels
import com.self.app.ui.adapter.BannerAdapter
import com.self.app.ui.adapter.HandicapAdapter
import com.self.app.ui.banner.BannerViewHolder
import com.self.app.ui.decoration.StickyHeaderItemDecoration
import com.self.app.ui.dialog.DialogManager
import com.self.app.ui.view.chart.ChartView
import com.self.app.viewmodel.HandicapViewModel
import com.self.app.viewmodel.bean.BannerResponse
import com.self.base.ui.BaseActivity
import com.zhpan.bannerview.BannerViewPager

class HandicapActivity : BaseActionBarActivity<HandicapViewModel, FragmentHandicapBinding>() {
    private val drawableSize:Int = 15
    private val handicapAdapter:HandicapAdapter by lazy { HandicapAdapter(arrayListOf()) }
    private lateinit var header:View

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mViewDataBinding.normalActionBar.backAction.setOnClickListener {
            finish()
        }
        mViewDataBinding.coinPairTv.run {
            val drawable= AppCompatResources.getDrawable(this@HandicapActivity,R.drawable.icon_more_coin_dui)
            drawable?.setBounds(0,0, getPixels(drawableSize,this@HandicapActivity),
                getPixels(drawableSize,this@HandicapActivity))
            setCompoundDrawables(drawable,null,null,null)
        }

        mViewDataBinding.handicapRecyclerView.run {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = handicapAdapter
            isNestedScrollingEnabled=true
            addItemDecoration(StickyHeaderItemDecoration(handicapAdapter))

        }


        header = LayoutInflater.from(this).inflate(R.layout.handicap_header, null).apply {
            mViewDataBinding.handicapRecyclerView.addHeaderView(this)
            mViewDataBinding.handicapRecyclerView.scrollToPosition(0)
        }
        val handler = Handler()
        val runnable = object : Runnable{
            override fun run() {
                mViewModel.reqHandicapInfo()
                handler.postDelayed(this,5000)
            }
        }
        handler.post(runnable)

    }


    override fun createDataObserver() {
        mViewModel.run {
            handicapInfos.observe(this@HandicapActivity, Observer {ret->
                Log.i("zml","ret=$ret")
                this@HandicapActivity.header.findViewById<ChartView>(R.id.handicap_buy).setChartModel(10f,ret)
                this@HandicapActivity.header.findViewById<ChartView>(R.id.handicap_sell).setChartModel(10f,ret)
            })
        }
    }

    override fun showLoading(message: String) {

    }

    override fun dismissLoading() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_handicap
    }




}

