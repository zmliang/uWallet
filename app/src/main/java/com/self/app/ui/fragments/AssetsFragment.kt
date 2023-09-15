package com.self.app.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.self.app.R
import com.self.app.appViewModel
import com.self.app.callback.LoadingCallback
import com.self.app.databinding.FragmentAssetsBinding
import com.self.app.databinding.FragmentHomeBinding
import com.self.app.databinding.FragmentMainBinding
import com.self.app.ex.SettingUtil
import com.self.app.ui.adapter.AssetAdapter
import com.self.app.ui.adapter.BannerAdapter
import com.self.app.ui.adapter.HomeAdapter
import com.self.app.ui.banner.BannerViewHolder
import com.self.app.ui.decoration.StickyHeaderItemDecoration
import com.self.app.ui.drawable.RoundedCornerBgDrawable
import com.self.app.ui.other.LoadingManager
import com.self.app.viewmodel.AssetViewModel
import com.self.app.viewmodel.MainViewModel
import com.self.app.viewmodel.bean.BannerResponse
import com.self.base.ex.loadListData
import com.self.base.ui.BaseFragment
import com.self.network.ex.parseState
import com.zhpan.bannerview.BannerViewPager

class AssetsFragment : BaseFragment<AssetViewModel,FragmentAssetsBinding>() {

    private val assetAdapter: AssetAdapter by lazy { AssetAdapter(arrayListOf()) }

    //界面状态管理者
    private lateinit var loadService: LoadService<Any>
    override fun initView(savedInstanceState: Bundle?) {
        Log.i("zml","资产页")
        mViewBind.assetRecyclerView.run {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = assetAdapter
            isNestedScrollingEnabled=true
            addItemDecoration(StickyHeaderItemDecoration(assetAdapter))
        }
        //状态页配置
        loadService = loadServiceInit(mViewBind.assetRefreshLayout) {
            //点击重试时触发的操作
            loadService.showCallback(LoadingCallback::class.java)
            //TODO 请求数据
            requestAllVm()
        }
    }



    override fun lazyLoadData() {
        //设置界面 加载中
        loadService.showCallback(LoadingCallback::class.java)
        requestAllVm()
    }

    fun requestAllVm(){
        mViewModel.reqTokenInfo(true)
    }

    override fun createObserver() {
        mViewModel.run {
            //监听首页文章列表请求的数据变化
            tokenInfoDataState.observe(viewLifecycleOwner, Observer {
                if (mViewBind.assetRecyclerView.headerCount<=0){
                    val headview = LayoutInflater.from(context).inflate(R.layout.asset_header, null).apply {
                        this.layoutParams =LayoutParams(MATCH_PARENT,WRAP_CONTENT)
                    }
                    headview.findViewById<TextView>(R.id.asset_in).background = RoundedCornerBgDrawable(mActivity.getDrawable(R.drawable.white_bg)!!,Color.WHITE)
                    headview.findViewById<TextView>(R.id.asset_out).background = RoundedCornerBgDrawable(mActivity.getDrawable(R.drawable.white_bg)!!,Color.WHITE)

                    mViewBind.assetRecyclerView.addHeaderView(headview)
                    mViewBind.assetRecyclerView.scrollToPosition(0)
                }
                loadListData(it, assetAdapter, loadService, mViewBind.assetRecyclerView)

            })
        }
    }

    override fun showLoading(message: String) {
        LoadingManager.instance.show(this,message)
    }


    override fun dismissLoading() {
        LoadingManager.instance.dismiss()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_assets
    }

    fun loadServiceInit(view: View, callback: () -> Unit): LoadService<Any> {
        val loadsir = LoadSir.getDefault().register(view) {
            //点击重试时触发的操作
            callback.invoke()
        }
        loadsir.showSuccess()
        SettingUtil.setLoadingColor(SettingUtil.getColor(this.mActivity), loadsir)
        return loadsir
    }

}