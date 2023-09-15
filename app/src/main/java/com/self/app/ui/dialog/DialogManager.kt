package com.self.app.ui.dialog

import android.app.Dialog
import android.media.session.MediaSession.Token
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.self.app.R
import com.self.app.getPixels
import com.self.app.ui.adapter.ChainAdapter
import com.self.app.ui.adapter.TokenSelectorAdapter
import com.self.app.ui.decoration.StickyHeaderItemDecoration
import com.self.app.viewmodel.bean.ChainInfo
import com.zml.baseui.VerticalTabLayout
import com.zml.wallet.TokenInfo


class DialogManager {


    companion object {
        val instance: DialogManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DialogManager()
        }
    }

    fun showWalletManager(context:AppCompatActivity):Dialog{
        val dialog = BottomSheetDialog(context,R.style.BottomSheetDialogStyle)
        val view = context.layoutInflater.inflate(R.layout.dialog_wallet_manager,null)
        dialog.setContentView(view)
        (view.parent as View).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            .layoutParams.height = getPixels(600,context)
        dialog.show()

        val adapter = ChainAdapter(arrayListOf(
            ChainInfo(name = "Polygon-1"),
            ChainInfo(name = "Polygon-2"),
            ChainInfo(name = "Polygon-3"),
            ChainInfo(name = "Polygon-4"),
            ChainInfo(name = "Polygon-5"),
            ChainInfo(name = "Polygon-6"),
            ChainInfo(name = "Polygon-7"),
            ChainInfo(name = "Polygon-8"),
        ))
        val tabLayout = view.findViewById<VerticalTabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager,null)


        return dialog
    }


    fun showTokens(context:AppCompatActivity,tokens:java.util.ArrayList<TokenInfo>,listener:(index:Int)->Unit){
        val dialog = BottomSheetDialog(context,R.style.BottomSheetDialogStyle)
        val view = context.layoutInflater.inflate(R.layout.dialog_token_selector,null)
        dialog.setContentView(view)


        val bottom = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        // 将BottomSheetDialog背景设为透明
        if (bottom != null) {
            bottom.setBackgroundResource(android.R.color.transparent)
            //解决BottomSheetDialog底部弹出框 横屏显示不全的问题
            val bottomSheetBehavior = BottomSheetBehavior.from(bottom)
            /**
             * 控制展开跟收缩
             *
             * STATE_EXPANDED 展开状态
             * STATE_COLLAPSED 收缩状态
             * STATE_DRAGGING 正在拖动状态
             * STATE_HIDDEN 隐藏状态
             */
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }


//        (view.parent as View).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//            .layoutParams.height = getPixels(500,context)

        val rv = view.findViewById<RecyclerView>(R.id.token_select_rv)

        rv.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled=true
            adapter = TokenSelectorAdapter(tokens).apply {
                setOnItemClickListener { _, _, position ->
                    listener.invoke(position)
                    dialog.dismiss()
                }
            }
            layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,false)
        }
        dialog.show()

    }


}