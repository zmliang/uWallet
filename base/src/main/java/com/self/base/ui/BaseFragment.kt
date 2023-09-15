package com.self.base.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.self.base.BaseViewModel
import com.self.base.net.NetState
import com.self.base.net.NetStateManager

open abstract class BaseFragment<VM:BaseViewModel, VB : ViewBinding>:Fragment() {

    lateinit var mActivity: AppCompatActivity

    lateinit var mViewModel:VM
    private var isFirst:Boolean=true
    private val mHandler = Handler()


    //该类绑定的 ViewBinding
    protected var _binding: VB? = null
    val mViewBind: VB get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding  = inflateBindingWithGeneric(inflater,container,false)

        return mViewBind.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true
        mViewModel = createViewModel()
        initView(savedInstanceState)
        createObserver()
        registorDefUIChange()
        initData()


    }



    /**
     * 初始化view
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载
     */
    abstract fun lazyLoadData()

    /**
     * 创建观察者
     */
    abstract fun createObserver()

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    private fun onVisible(){
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            // 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿
            mHandler.postDelayed( {
                lazyLoadData()
                //在Fragment中，只有懒加载过了才能开启网络变化监听
                NetStateManager.instance.mNetworkStateCallback.observeInFragment(
                    this
                ) {
                    //不是首次订阅时调用方法，防止数据第一次监听错误
                    if (!isFirst) {
                        onNetworkStateChanged(it)
                    }
                }
                isFirst = false
            },lazyLoadTime())
        }
    }

    /**
     * Fragment执行onCreate后触发的方法
     */
    open fun initData() {}

    abstract fun showLoading(message: String = "请求网络中...")

    abstract fun dismissLoading()

    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
        mViewModel.showLoading.observeInFragment(this, Observer {
            showLoading(it)
        })
        mViewModel.dismissLoading.observeInFragment(this, Observer {
            dismissLoading()
        })
    }

    /**
     * 将非该Fragment绑定的ViewModel添加 loading回调 防止出现请求时不显示 loading 弹窗bug
     * @param viewModels Array<out BaseViewModel>
     */
    protected fun addLoadingObserve(vararg viewModels: BaseViewModel) {
        viewModels.forEach { viewModel ->
            //显示弹窗
            viewModel.showLoading.observeInFragment(this, Observer {
                showLoading(it)
            })
            //关闭弹窗
            viewModel.dismissLoading.observeInFragment(this, Observer {
                dismissLoading()
            })
        }
    }

    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    open fun onNetworkStateChanged(netState: NetState) {}


    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     * @return Long
     */
    open fun lazyLoadTime(): Long {
        return 300
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

    fun navigate(): NavController {
        return NavHostFragment.findNavController(this)
    }


    abstract fun getLayoutId():Int
}