package com.self.app.ui.activity

import android.R
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.self.app.appViewModel
import com.self.app.databinding.ActivitySplashBinding
import com.self.app.ui.adapter.ChainAdapter
import com.self.app.ui.adapter.WalletItemAdapter
import com.self.app.viewmodel.bean.ChainInfo
import com.zml.baseui.view.FloatingMenu.MenuClickListener
import com.zml.wallet.WalletInfo


class SplashActivity : AppCompatActivity() {
    lateinit var  binding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =DataBindingUtil.setContentView(this,com.self.app.R.layout.activity_splash)

//        binding.importMne.setOnClickListener{
//            appViewModel.importMnemonic("gossip piece elevator across buyer render cousin goat smart awkward brain arch")
//        }
//
//        binding.query.setOnClickListener{
//            appViewModel.reqAllWalletAccounts()
//        }

        appViewModel.currentWallet.observeInActivity(this) {
            Log.i("zml", "当前钱包=$it")
        }

//        appViewModel.walletAccounts.observeInActivity(this) {
//
//            Log.i("zml", "共有多少个钱包=${it.size}")
//        }

        binding.walletHeaderRv.run {
            val walletInfos: MutableList<WalletInfo> = arrayListOf(
                WalletInfo(),
                WalletInfo(),
                WalletInfo(),
                WalletInfo(),
                WalletInfo(),
                WalletInfo(),
                WalletInfo(),
                WalletInfo(),
                WalletInfo(),
                WalletInfo(),
                WalletInfo(),
                WalletInfo(),
                WalletInfo(),
                WalletInfo(),
                WalletInfo(),
            )
            setHasFixedSize(true)
            isNestedScrollingEnabled=true
            adapter = WalletItemAdapter(walletInfos)
            layoutManager = LinearLayoutManager(this@SplashActivity,LinearLayoutManager.HORIZONTAL,false)
            LinearSnapHelper().attachToRecyclerView(this)
        }

        binding.walletViewPager.run {
            val _adapter = ChainAdapter(arrayListOf(
                ChainInfo(name = "Polygon-1"),
                ChainInfo(name = "Polygon-2"),
                ChainInfo(name = "Polygon-3"),
                ChainInfo(name = "Polygon-4"),
                ChainInfo(name = "Polygon-5"),
                ChainInfo(name = "Polygon-6"),
                ChainInfo(name = "Polygon-7"),
                ChainInfo(name = "Polygon-8"),
            ))
            //setHasFixedSize(true)
            isNestedScrollingEnabled=true
            adapter = _adapter
//            layoutManager = LinearLayoutManager(this@SplashActivity,
//                LinearLayoutManager.VERTICAL,false)
        }

//        binding.walletRv.run {
//            layoutManager = LinearLayoutManager(context)
//            setHasFixedSize(true)
//            adapter = HomeAdapter(arrayListOf())
//            isNestedScrollingEnabled=true


//            headview.findViewById<ViewPager2>(com.self.app.R.id.viewPager).apply {
//                val _adapter = ChainAdapter(arrayListOf(
//                    ChainInfo(name = "Polygon-1"),
//                    ChainInfo(name = "Polygon-2"),
//                    ChainInfo(name = "Polygon-3"),
//                    ChainInfo(name = "Polygon-4"),
//                    ChainInfo(name = "Polygon-5"),
//                    ChainInfo(name = "Polygon-6"),
//                    ChainInfo(name = "Polygon-7"),
//                    ChainInfo(name = "Polygon-8"),
//
//                ))
//                adapter = _adapter
//            }



//            val footerView = LayoutInflater.from(context).inflate(com.self.app.R.layout.wallet_footer_layout,null).apply {
//                val _adapter = ChainAdapter(arrayListOf(
//                    ChainInfo(name = "Polygon-1"),
//                    ChainInfo(name = "Polygon-2"),
//                    ChainInfo(name = "Polygon-3"),
//                    ChainInfo(name = "Polygon-4"),
//                    ChainInfo(name = "Polygon-5"),
//                    ChainInfo(name = "Polygon-6"),
//                    ChainInfo(name = "Polygon-7"),
//                    ChainInfo(name = "Polygon-8"),
//
//                ))
//                this.findViewById<ViewPager2>(com.self.app.R.id.wallet_footer).adapter = _adapter
//            }
//            addFooterView(footerView)


//        }
//
//        val headview = LayoutInflater.from(this).inflate(com.self.app.R.layout.wallet_header_layout, null)

//        headview.findViewById<RecyclerView>(com.self.app.R.id.wallet_header_rv).apply {
//            val walletInfos: MutableList<WalletInfo> = arrayListOf(
//                WalletInfo(),
//                WalletInfo(),
//                WalletInfo(),
//                WalletInfo(),
//                WalletInfo(),
//                WalletInfo(),
//                WalletInfo(),
//                WalletInfo(),
//                WalletInfo(),
//                WalletInfo(),
//                WalletInfo(),
//                WalletInfo(),
//                WalletInfo(),
//                WalletInfo(),
//                WalletInfo(),
//            )
//            adapter = WalletItemAdapter(walletInfos)
//            layoutManager = LinearLayoutManager(this@SplashActivity,LinearLayoutManager.HORIZONTAL,false)
//            LinearSnapHelper().attachToRecyclerView(this)
//        }
//        headview.findViewById<RecyclerView>(com.self.app.R.id.wallet_token_rv).apply {
//            val _adapter = ChainAdapter(arrayListOf(
//                ChainInfo(name = "Polygon-1"),
//                ChainInfo(name = "Polygon-2"),
//                ChainInfo(name = "Polygon-3"),
//                ChainInfo(name = "Polygon-4"),
//                ChainInfo(name = "Polygon-5"),
//                ChainInfo(name = "Polygon-6"),
//                ChainInfo(name = "Polygon-7"),
//                ChainInfo(name = "Polygon-8"),
//                ))
//                adapter = _adapter
//                layoutManager = LinearLayoutManager(this@SplashActivity,
//                    LinearLayoutManager.VERTICAL,false)
//        }
//
//        binding.walletRv.addHeaderView(headview)
//
//        binding.walletRv.scrollToPosition(0)



        binding.floatingMenu
            .setSubMenuBackgroundResource(com.self.app.R.drawable.pink_circle_button_click)
            .addSubMenu(R.drawable.ic_menu_my_calendar, Action("Opening calendar..."))
            .addSubMenu(R.drawable.ic_menu_call, Action("Calling Jeffrey..."))
            .addSubMenu(R.drawable.ic_menu_manage, Action("Opening settings..."))
            .setSubMenuBetweenPadding(250)
            .setMenuExtraMargin(50)
            .setMenuTextColor(getColor(R.color.holo_green_light))
            .setOnMenuClickListener(MenuClickListener { coverView, isOpen ->
                var rotateAnim: RotateAnimation? = null
                rotateAnim = if (isOpen) {
                    RotateAnimation(45f, 0f, (coverView.height / 2).toFloat(),
                        (coverView.width / 2).toFloat()
                    )
                } else {
                    RotateAnimation(0f, 45f, (coverView.height / 2).toFloat(),
                        (coverView.width / 2).toFloat()
                    )
                }
                rotateAnim.duration = 200
                rotateAnim.interpolator = OvershootInterpolator()
                rotateAnim.fillAfter = true
                coverView.startAnimation(rotateAnim)
            })


        //val nativeLib= NativeLib()
        //nativeLib.init(this)

        //val walletKey = "0370e03f78121d109220d52fd9d6437a01e6a6c813684c43427bc91b6775f660dd"

        //val walletKey = nativeLib.importPrivateKey("",60)
        //Log.i("zml","walletKey:$walletKey")

        //val walletKey = nativeLib.importPrivateKey("a7a56f9e331affdd2f9c1c74ab5df780a75c1b93f12a0c9056196c971af22ef5",60)
        //Log.i("zml","walletKey:$walletKey")

        //val walletKey = nativeLib.importMnemonic("gossip piece elevator across buyer render cousin goat smart awkward brain arch","")

        //val privateKey = nativeLib.getPublicChainPrivateKey(walletKey,"",60)//私钥
        //Log.i("zml","privateKey:$privateKey")

        //val publicKey = nativeLib.getPublicChainPublicKey(walletKey,"",60)//公钥
        //Log.i("zml", "publicKey=${publicKey["secp256k1"]}")

        //val address = nativeLib.getPublicChainAddress(walletKey,"",60)//钱包地址
        //Log.i("zml", "address=$address")

        Handler(Looper.getMainLooper()).postDelayed( {
            //startActivity(Intent(this,MainActivity::class.java))
        },3000)

        initRv()

    }



    fun initRv(){

    }


    private class Action(var mText: String) : View.OnClickListener {
        override fun onClick(v: View?) {
            Toast.makeText(v?.context, mText, Toast.LENGTH_SHORT).show()
        }
    }


}