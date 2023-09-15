package com.self.base.ui

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.self.base.BaseViewModel
import com.self.base.net.NetState
import com.self.base.net.NetStateManager
import com.self.base.utils.KeyboardsUtils
import com.self.base.utils.notNull

abstract class BaseActivity<VM: BaseViewModel, VB:ViewBinding>: AppCompatActivity(){

    lateinit var mViewModel: VM
    lateinit var mViewDataBinding: VB



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isTaskRoot && intent.action!=null){
            finish()
            return
        }
        initBinding().notNull(
            {setContentView(it)},
            {setContentView(getLayoutId())})

        mViewModel = ViewModelProvider(this).get(getVmClazz(this))
        initView(savedInstanceState)
        registerNotifyLoading()
        createDataObserver()
        NetStateManager.instance.mNetworkStateCallback.observeInActivity(this) {
            onNetStateChanged(it)
        }
    }

    abstract fun createDataObserver()

    private fun registerNotifyLoading() {
        //显示弹窗
        mViewModel.showLoading.observeInActivity(this, Observer {
            showLoading(it)
        })
        //关闭弹窗
        mViewModel.dismissLoading.observeInActivity(this, Observer {
            dismissLoading()
        })
    }

    abstract fun showLoading(message:String = "Loading...")

    abstract fun dismissLoading()

    abstract fun getLayoutId():Int

    abstract fun initView(savedInstanceState: Bundle?)

    open fun onNetStateChanged(netState: NetState){}

    private fun initBinding(): View? {
        mViewDataBinding = inflateBindingWithGeneric(layoutInflater)
        return mViewDataBinding.root
    }


    /**
     * 将非该Activity绑定的ViewModel添加 loading回调 防止出现请求时不显示 loading 弹窗bug
     * @param viewModels Array<out BaseViewModel>
     */
    protected fun addLoadingObserve(vararg viewModels: BaseViewModel) {
        viewModels.forEach { viewModel ->
            //显示弹窗
            viewModel.showLoading.observeInActivity(this, Observer {
                showLoading(it)
            })
            //关闭弹窗
            viewModel.dismissLoading.observeInActivity(this, Observer {
                dismissLoading()
            })
        }
    }


    @CallSuper
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            view?.let {
                if (KeyboardsUtils.isShouldHideKeyBord(view, ev)) {
                    KeyboardsUtils.hintKeyBoards(view)
                }
            }

        }
        return super.dispatchTouchEvent(ev)
    }


}