package com.self.app.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.self.app.R
import com.self.app.appViewModel
import com.self.app.callback.LoadingCallback
import com.self.app.databinding.TestCoordBinding
import com.self.app.eventViewModel
import com.self.app.ex.SettingUtil
import com.self.app.getPixels
import com.self.app.ui.adapter.AssetAdapter
import com.self.app.ui.adapter.WalletItemAdapter
import com.self.app.ui.decoration.StickyHeaderItemDecoration
import com.self.app.ui.dialog.QRCodeDialog
import com.self.app.viewmodel.WalletViewModel
import com.self.base.ex.loadListData
import com.yanzhenjie.recyclerview.*
import com.zml.wallet.WALLET_PLACEHOLDER
import com.zml.wallet.WalletInfo
import java.lang.Float.max
import java.math.BigDecimal
import java.util.ArrayList
import kotlin.math.abs


class ScreenActivity: BaseLoadingActivity<WalletViewModel, TestCoordBinding>(){

    private val walletAdapter:WalletItemAdapter by lazy { WalletItemAdapter(arrayListOf()) }

    private val  tokenAdapter: AssetAdapter by lazy { AssetAdapter(arrayListOf()) }

    val snapHelper = LinearSnapHelper()
    private lateinit var walletRv:SwipeRecyclerView

    //界面状态管理者
    private lateinit var loadService: LoadService<Any>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
    }

    override fun createDataObserver() {

        //所有钱包
        mViewModel.walletAccounts.observe(this) {
            Log.i("zml","所有钱包=${it}")
            walletAdapter.setList(it.listData)
            //mViewDataBinding.rvMain.scrollToPosition(0)
        }

        //所有代币
        mViewModel.tokenData.observe(this){
            mViewDataBinding.refreshLayout.finishRefresh()
            loadListData(it,tokenAdapter,loadService,mViewDataBinding.rvMain)
            updateBalance()
        }

        //当前选中的钱包发生了变化
        appViewModel.currentWallet.observeInActivity(this){
            Log.i("zml","钱包变化，重新请求币种信息")
            mViewModel.requestTokenInfo(true,it)
        }

        //新添加了个钱包
        eventViewModel.updateCurWalletEvent.observeInActivity(this){
            if (walletAdapter.data[0]== WALLET_PLACEHOLDER){
                walletAdapter.setData(0,it)
            }else{
                walletAdapter.addData(0,it)
            }
            walletRv.smoothScrollToPosition(0)
            updateCurWallet(it)
        }

        //删除钱包
        eventViewModel.deleteWalletEvent.observeInActivity(this){
            appViewModel.currentWallet.value?.let { it1 ->
                walletAdapter.remove(it1)
                if (walletAdapter.data.size<=0){
                    walletAdapter.addData(WALLET_PLACEHOLDER)
                }
                walletRv.smoothScrollToPosition(0)
                updateBalance()
            }
        }

        //添加了新代币
        eventViewModel.addTokenEvent.observeInActivity(this){
            tokenAdapter.addData(it)
            mViewModel.tokenData.value?.listData?.add(it)
            updateBalance()
        }

        //删除代币
        eventViewModel.deleteTokenEvent.observeInActivity(this){
            tokenAdapter.remove(it)
            mViewModel.tokenData.value?.listData?.remove(it)
            updateBalance()
        }

        //钱包名称更新
        eventViewModel.updateWalletNameEvent.observeInActivity(this){
            val itemView = snapHelper.findSnapView(walletRv.layoutManager)
            val position = walletRv.getChildAdapterPosition(itemView!!)

            appViewModel.currentWallet.value?.let {it1->
                run {
                    it1.walletName = it
                    walletAdapter.setData(position, it1)
                }

            }

        }


    }

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private val swipeMenuCreator: SwipeMenuCreator = object : SwipeMenuCreator {
        override fun onCreateMenu(
            swipeLeftMenu: SwipeMenu,
            swipeRightMenu: SwipeMenu,
            position: Int
        ) {
            val size =getPixels(60,this@ScreenActivity)

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            run {
                val deleteItem: SwipeMenuItem =
                    SwipeMenuItem(this@ScreenActivity)
                        .setBackground(R.drawable.selector_red)
                        .setImage(R.drawable.ic_action_delete)
                        //.setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(size)
                        .setHeight(size)
                swipeRightMenu.addMenuItem(deleteItem) // 添加菜单到右侧。
            }
        }
    }

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private val mMenuItemClickListener =
        OnItemMenuClickListener { menuBridge, position ->

            val direction = menuBridge.direction // 左侧还是右侧菜单。
            val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                mViewModel.requestDeleteToken(tokenAdapter.getItem(position),menuPosition)
            }
        }

    fun updateCurWallet(walletInfo: WalletInfo){
        if (walletInfo == appViewModel.currentWallet.value){

            return
        }
        appViewModel.currentWallet.postValue(walletInfo)
    }


    override fun getLayoutId(): Int {
        return R.layout.test_coord
    }

    override fun initView(savedInstanceState: Bundle?) {

        mViewDataBinding.refreshLayout.apply {
            setEnableLoadMore(false)//是否启用上拉加载功能
            setEnableOverScrollBounce(true)//是否启用越界回弹
            setEnableOverScrollDrag(true)//是否启用越界拖动

            setOnRefreshListener {
                appViewModel.currentWallet.value?.let { it1 ->
                    mViewModel.requestTokenInfo(true,
                        it1
                    )
                }
            }

        }

        walletRv = LayoutInflater.from(this@ScreenActivity).inflate(R.layout.single_recyclerview, null) as SwipeRecyclerView

        walletRv.run {
            setHasFixedSize(true)
            isNestedScrollingEnabled=true
            adapter = walletAdapter
            layoutManager = LinearLayoutManager(this@ScreenActivity,
                LinearLayoutManager.HORIZONTAL,false)

            snapHelper.attachToRecyclerView(this)

            //监听 滚动 获取具体位置
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val itemView = snapHelper.findSnapView(recyclerView.layoutManager)
                        val position = recyclerView.getChildAdapterPosition(itemView!!)
                        val w =  walletAdapter.getItem(position)
                        updateCurWallet(w)
                        Log.i("zml", "the current wallet is: ${appViewModel.currentWallet.value}")
                    }
                }
            })
        }

        walletAdapter.setOnItemClickListener { adapter, view, position ->

        }

        mViewDataBinding.rvMain.run {
            setHasFixedSize(true)


            setSwipeMenuCreator(swipeMenuCreator)
            setOnItemMenuClickListener(mMenuItemClickListener)

            isNestedScrollingEnabled=true
            adapter = tokenAdapter
            layoutManager = LinearLayoutManager(this@ScreenActivity,
                LinearLayoutManager.VERTICAL,false)
            //LinearSnapHelper().attachToRecyclerView(this)
            addItemDecoration(StickyHeaderItemDecoration(tokenAdapter))
            addHeaderView(walletRv)

        }


        tokenAdapter.setOnItemClickListener { adapter, view, position ->
            Log.i("zml","pos=$position")
            if (position<=1){
                return@setOnItemClickListener
            }
            startActivity(Intent(this,TokenTransEventActivity::class.java).putExtra("token",tokenAdapter.data[position-1]))
        }


        mViewDataBinding.receive.setOnClickListener {

            appViewModel.currentWallet.value?.address?.let { it1 ->
                if (it1.isEmpty())ToastUtils.showShort("没有钱包")else QRCodeDialog.show(it1,this)
            }
        }

        mViewDataBinding.send.setOnClickListener {
            val datas =mViewModel.tokenData.value?.listData

            if (datas == null || datas.isEmpty()){
                return@setOnClickListener
            }
            startActivity(Intent(this,TransferActivity::class.java)
                .putParcelableArrayListExtra("all_token",
                    datas?.subList(1,datas.size)?.let { it1 -> ArrayList(it1) }))
        }

        val BASE = 120
        mViewDataBinding.ablTitle.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val scale =1 - abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange
            val scaleFactor = max(0.5f,
               scale )

            //mViewDataBinding.receive.scaleX = scaleFactor
            //mViewDataBinding.receive.scaleY = scaleFactor

            //mViewDataBinding.send.scaleX = scaleFactor
            //mViewDataBinding.send.scaleY = scaleFactor

            //mViewDataBinding.receive.translationX = (1-scaleFactor)*300
            //mViewDataBinding.send.translationX = -(1-scaleFactor)*300

            mViewDataBinding.receive.translationY = (1-scaleFactor)*50
            mViewDataBinding.send.translationY = (1-scaleFactor)*50
            mViewDataBinding.receive.alpha = scale
            mViewDataBinding.send.alpha = scale

            mViewDataBinding.totalBalance.scaleX = scaleFactor
            mViewDataBinding.totalBalance.scaleY = scaleFactor

            mViewDataBinding.totalBalance.translationY = (1-scaleFactor)*BASE
            mViewDataBinding.totalBalance.translationY = (1-scaleFactor)*BASE

        }


        mViewDataBinding.addWalletIv.setOnClickListener {
            startActivity(Intent(this,AddWalletActivity::class.java))
        }


        mViewDataBinding.chainSpinner.apply {
            adapter = ArrayAdapter.createFromResource(this@ScreenActivity,
                R.array.support_chains,android.R.layout.simple_spinner_item)

        }

        //状态页配置

        loadService = loadServiceInit(mViewDataBinding.refreshLayout) {
            //点击重试时触发的操作
            loadService.showCallback(LoadingCallback::class.java)
            //TODO 请求数据
            mViewModel.requestWalletInfo(true)
            appViewModel.currentWallet.value?.let { mViewModel.requestTokenInfo(true, it) }
        }

        mViewModel.requestWalletInfo(true)
        appViewModel.currentWallet.value?.let { mViewModel.requestTokenInfo(true, it) }

    }

    fun loadServiceInit(view: View, callback: () -> Unit): LoadService<Any> {
        val loadsir = LoadSir.getDefault().register(view) {
            //点击重试时触发的操作
            callback.invoke()
        }
        loadsir.showSuccess()
        SettingUtil.setLoadingColor(SettingUtil.getColor(this), loadsir)
        return loadsir
    }


    fun updateBalance(){
        var total = BigDecimal.ZERO
        for (item in tokenAdapter.data){
            item.balance?.let {
                BigDecimal(it)
            }?.let {
                total = total.add(it)
            }
        }
        mViewDataBinding.totalBalance.setBalance("$${total}")
    }
}

