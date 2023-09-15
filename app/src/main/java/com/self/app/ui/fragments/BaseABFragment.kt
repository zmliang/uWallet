package com.self.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.self.app.databinding.FragmentBaseBinding
import com.self.base.BaseViewModel
import com.self.base.ui.BaseFragment

open abstract class BaseABFragment<VM:BaseViewModel, VB : ViewBinding>:BaseFragment<VM,VB>() {


    private lateinit var fragmentBaseBinding:FragmentBaseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBaseBinding = FragmentBaseBinding.inflate(inflater, container, false)

       val child = super.onCreateView(
            inflater,
            container,
            savedInstanceState
        )
        fragmentBaseBinding.fragmentContentContainer.addView(child)
        return fragmentBaseBinding.root
    }

    open fun updateCenterTitle(title:String){
        fragmentBaseBinding.incNormalActionbar.titleAction.text = title
    }

}