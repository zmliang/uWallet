//package com.self.app.ui.fragments
//
//import android.graphics.drawable.ColorDrawable
//import android.os.Bundle
//import com.self.app.R
//import com.self.app.databinding.FragmentHandicapBinding
//import com.self.app.databinding.NormalActionBarBinding
//import com.self.app.ex.SettingUtil
//import com.self.app.viewmodel.HandicapViewModel
//import com.self.base.ui.BaseFragment
//
//class HandicapFragment:BaseFragment<HandicapViewModel,FragmentHandicapBinding>() {
//    override fun initView(savedInstanceState: Bundle?) {
//
//        mActivity.supportActionBar?.displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
//        mActivity.supportActionBar?.customView = NormalActionBarBinding.inflate(layoutInflater).root
//        mActivity.supportActionBar?.setBackgroundDrawable(ColorDrawable(SettingUtil.getColor(mActivity,R.color.white)))
//
//    }
//
//    override fun lazyLoadData() {
//
//    }
//
//    override fun createObserver() {
//
//    }
//
//    override fun showLoading(message: String) {
//
//    }
//
//    override fun dismissLoading() {
//
//    }
//
//    override fun getLayoutId(): Int {
//        return R.layout.fragment_handicap
//    }
//}