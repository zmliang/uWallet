//package com.self.app.ui.dialog
//
//import android.content.Context
//import android.content.res.Configuration
//import android.os.Bundle
//import android.view.View
//import com.google.android.material.bottomsheet.BottomSheetBehavior
//import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
//import com.google.android.material.bottomsheet.BottomSheetDialog
//import com.self.app.R
//import com.self.app.databinding.DialogWalletManagerBinding
//
//class WalletManagerDialog(context: Context) :BottomSheetDialog(context){
//   // private lateinit var mBinding:DialogWalletManagerBinding
//
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //mBinding = DialogWalletManagerBinding.inflate(layoutInflater)
//        setContentView()
//
//        //BottomSheetDialog有五种状态：
//        //STATE_COLLAPSED： 折叠状态
//        //STATE_EXPANDED： 展开状态
//        //STATE_DRAGGING ： 过渡状态
//        //STATE_SETTLING： 视图从脱离手指自由滑动到最终停下的这一小段时间
//        //STATE_HIDDEN ：启用后用户将能通过向下滑动完全隐藏 bottom sheet
//        //由于我们支持横屏状态，横屏状态下布局高度会超过屏幕的高，可以将
//        //behavior设置为STATE_EXPANDED，然后在Layout中添加一个ScrollView
//        //behavior.state = STATE_EXPANDED
//
//        //判断当前Activity是横屏还是竖屏
//        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            //今日头条的屏幕适配思路，自定义弹窗的宽
//            //DensityUtils.setSheetDialog(payConfirmBottomDialog, activity, 360)
//        } else {
//            //DensityUtils.setSheetDialog(payConfirmBottomDialog, activity, 500)
//        }
//
//    }
//
//
//
//}

