package com.self.app.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.self.app.R
import com.self.app.appViewModel
import com.self.app.databinding.FragmentMainBinding
import com.self.app.viewmodel.MainViewModel
import com.self.base.ui.BaseFragment

class MainFragment : BaseFragment<MainViewModel,FragmentMainBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.mainViewpager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                when (position) {
                    0 -> {
                        return HomeFragment()
                    }
                    1 -> {
                        return AssetsFragment()
                    }
                    2 -> {
                        return DiscoverFragment()
                    }
                    3 -> {
                        return MeFragment()
                    }
                    else -> {
                        return HomeFragment()
                    }
                }
            }
            override fun getItemCount() = 4
        }

        mViewBind.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_main -> mViewBind.mainViewpager.setCurrentItem(0, false)
                R.id.menu_asset -> mViewBind.mainViewpager.setCurrentItem(1, false)
                R.id.menu_discover -> mViewBind.mainViewpager.setCurrentItem(2, false)
                R.id.menu_me -> mViewBind.mainViewpager.setCurrentItem(3, false)
            }
            true
        }
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {
        appViewModel.appColor.observeInFragment(this, Observer {
            //setUiTheme(it, mainBottom)
        })

//        appViewModel.walletAccounts.observeInFragment(this) {
//            it.let {
//                Log.i("zml", "size=${it.size}")
////                it.forEach {
////                    Log.i("zml", it.toString())
////                }
//            }
//
//        }
    }

    override fun showLoading(message: String) {

    }

    override fun dismissLoading() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

}