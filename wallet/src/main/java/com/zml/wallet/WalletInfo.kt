package com.zml.wallet

import android.os.Parcelable
import com.zml.wallet.entity.WalletEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class WalletInfo(var chainName:String,
                      var chainID:String,
                      var walletKey:String?,
                      var passWord:String?,
                      var mode:Int?,
                      var walletName:String?,
                      var address:String,
                      var publicKey:String?,
                      var isFresh:Boolean = true) : Parcelable {
    constructor(walletEntity: WalletEntity,addr:String,publicKey:String?,walletName: String?):this(chainName = walletEntity.chainName.toString(),
        chainID= walletEntity.chainID.toString(),
        walletKey= walletEntity.walletKey,
        passWord= walletEntity.passWord,
        mode= walletEntity.mode,
        walletName= walletName,
        address = addr,
        publicKey = publicKey
        )

    constructor(walletEntity: WalletEntity,addr:String,publicKey:String?):this(chainName = walletEntity.chainName.toString(),
        chainID= walletEntity.chainID.toString(),
        walletKey= walletEntity.walletKey,
        passWord= walletEntity.passWord,
        mode= walletEntity.mode,
        walletName= walletEntity.walletName,
        address = addr,
        publicKey = publicKey
    )

    constructor():this(chainName = "null",
        chainID= "null",
        walletKey= null,
        passWord= null,
        mode= null,
        walletName= null,
        address = "",
        publicKey = null)
}

var WalletInfo.isPlaceholder:Boolean
    get() =  mode == -1
    set(value) {mode == -1}

val WALLET_PLACEHOLDER = WalletInfo().apply { mode = -1 }

