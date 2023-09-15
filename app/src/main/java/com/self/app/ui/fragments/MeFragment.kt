package com.self.app.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.self.app.R
import com.self.app.appViewModel
import com.self.app.databinding.FragmentAssetsBinding
import com.self.app.databinding.FragmentHomeBinding
import com.self.app.databinding.FragmentMainBinding
import com.self.app.databinding.FragmentMeBinding
import com.self.app.ui.adapter.MeAdapter
import com.self.app.ui.decoration.StickyHeaderItemDecoration
import com.self.app.viewmodel.MainViewModel
import com.self.app.viewmodel.bean.PageMe
import com.self.app.viewmodel.bean.PageMeInfo
import com.self.base.appContext
import com.self.base.ui.BaseFragment
import java.io.BufferedReader
import java.io.InputStreamReader

class MeFragment : BaseFragment<MainViewModel,FragmentMeBinding>() {

    private val meAdapter:MeAdapter by lazy { MeAdapter(arrayListOf()) }


    private fun parseConfig():PageMe{

        val pageConfigs = appContext.assets.open("me_page_config.json").bufferedReader().use{
            it.readText()
        }
        val gson = GsonBuilder().create()
        return gson.fromJson(pageConfigs, PageMe::class.java)
    }



    override fun initView(savedInstanceState: Bundle?) {
        val configs = parseConfig()
        mViewBind.meRecyclerView.run {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            meAdapter.setList(configs.items)
            adapter = meAdapter
            isNestedScrollingEnabled=true
        }

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
        return R.layout.fragment_me
    }

}