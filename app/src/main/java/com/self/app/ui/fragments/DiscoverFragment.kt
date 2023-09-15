package com.self.app.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.self.app.R
import com.self.app.appViewModel
import com.self.app.databinding.FragmentAssetsBinding
import com.self.app.databinding.FragmentDiscoverBinding
import com.self.app.databinding.FragmentHomeBinding
import com.self.app.databinding.FragmentMainBinding
import com.self.app.viewmodel.MainViewModel
import com.self.base.ui.BaseFragment

class DiscoverFragment : BaseFragment<MainViewModel,FragmentDiscoverBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mActivity.supportActionBar?.hide()
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {

    }

    override fun showLoading(message: String) {

    }

    override fun dismissLoading() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_discover
    }

}