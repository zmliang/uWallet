package com.self.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.self.app.R
import com.self.app.callback.LoadingCallback
import com.self.app.databinding.FragmentHomeBinding
import com.self.app.ex.SettingUtil
import com.self.app.ui.activity.HandicapActivity
import com.self.app.ui.adapter.BannerAdapter
import com.self.app.ui.adapter.HomeAdapter
import com.self.app.ui.banner.BannerViewHolder
import com.self.app.ui.decoration.StickyHeaderItemDecoration
import com.self.app.ui.dialog.DialogManager
import com.self.app.ui.other.LoadingManager
import com.self.app.viewmodel.HomeViewModel
import com.self.app.viewmodel.bean.BannerResponse
import com.self.base.ex.loadListData
import com.self.base.ui.BaseFragment
import com.self.network.ex.parseState
import com.zhpan.bannerview.BannerViewPager

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    private val homeAdapter:HomeAdapter by lazy { HomeAdapter(arrayListOf()) }

    //界面状态管理者
    private lateinit var loadService: LoadService<Any>

    //private val requestHomeViewModel: HomeViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.recyclerView.run {

            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = homeAdapter
            isNestedScrollingEnabled=true
            //因为首页要添加轮播图，所以我设置了firstNeedTop字段为false,即第一条数据不需要设置间距
            //addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f), false))
            //吸顶效果
            addItemDecoration(StickyHeaderItemDecoration(homeAdapter))
        }


        //状态页配置
        loadService = loadServiceInit(mViewBind.refreshLayout) {
            //点击重试时触发的操作
            loadService.showCallback(LoadingCallback::class.java)
            //TODO 请求数据
            requestAllVm()
        }

        homeAdapter.run {
            setOnItemClickListener { adapter, view, position ->

                startActivity(Intent(this@HomeFragment.mActivity, HandicapActivity::class.java))
//                navigate().navigate(R.id.action_to_webFragment, Bundle().apply {
//                    putParcelable(
//                        "curToken",
//                        homeAdapter.data[position]
//                    )
//                })
            }
        }


        mViewBind.incHomeToolbar.walletName.setOnClickListener {
            Log.i("zml","添加，更换钱包")
            DialogManager.instance.showWalletManager(mActivity)

        }

    }



    override fun lazyLoadData() {
        //设置界面 加载中
        loadService.showCallback(LoadingCallback::class.java)
        requestAllVm()
    }

    fun requestAllVm(){
        mViewModel.reqBannerInfo()
        mViewModel.reqTokenInfo(true)
    }

    override fun createObserver() {
        mViewModel.run {
            //监听首页文章列表请求的数据变化
            tokenInfoDataState.observe(viewLifecycleOwner, Observer {

                //设值 新写了个拓展函数，搞死了这个恶心的重复代码
                loadListData(it, homeAdapter, loadService, mViewBind.recyclerView)
            })
            //监听轮播图请求的数据变化
            bannerData.observe(viewLifecycleOwner, Observer { resultState ->
                parseState(resultState, { data ->

                    //请求轮播图数据成功，添加轮播图到headview ，如果等于0说明没有添加过头部，添加一个
                    if (mViewBind.recyclerView.headerCount == 0) {
                        val headview = LayoutInflater.from(context).inflate(R.layout.banner, null).apply {
                            findViewById<BannerViewPager<BannerResponse, BannerViewHolder>>(R.id.banner_view).apply {
                                adapter = BannerAdapter()
                                setLifecycleRegistry(lifecycle)
                                setOnPageClickListener {
                                    //navigate().navigate(R.id.action_to_webFragment, Bundle().apply {putParcelable("bannerdata", data[it])})
                                }
                                create(data)
                            }
                        }
                        mViewBind.recyclerView.addHeaderView(headview)
                        mViewBind.recyclerView.scrollToPosition(0)
                    }
                })
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
        return R.layout.fragment_home
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