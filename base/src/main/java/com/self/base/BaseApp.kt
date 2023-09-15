package com.self.base

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

open class BaseApp: Application() ,ViewModelStoreOwner{

    private lateinit var modelStore: ViewModelStore

    private var factory:ViewModelProvider.Factory? = null

    override fun onCreate() {
        super.onCreate()
        modelStore = ViewModelStore()
    }

    override fun getViewModelStore(): ViewModelStore {
        return modelStore
    }

    /**
     * 可以不使用baseApp。自己实现全局的viewModel也可以
     */
    fun getAppViewModelProvider():ViewModelProvider{
        return ViewModelProvider(this,getFactory())
    }

    fun getFactory():ViewModelProvider.Factory{
        if (factory == null){
            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return factory as ViewModelProvider.Factory
    }

}