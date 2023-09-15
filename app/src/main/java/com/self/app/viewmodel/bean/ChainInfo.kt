package com.self.app.viewmodel.bean

import android.os.Parcelable
import com.zml.wallet.WalletInfo
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ChainInfo(
    var name:String = "Polygon",
    var property:Int = 0,
    var walletInfos: MutableList<WalletInfo> = arrayListOf(
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
    ) : Parcelable
