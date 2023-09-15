package com.zml.wallet

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chain(var name:String?="Polygon",
                 var chainId:String="0x1",
                 var rpcs:List<String> = arrayListOf(),
                 var symbol:String? = "",
                 var browserRpc:String? = "",
                 var browserKey:String? = "",
                 var blockBrowser:String? = "",
                 var rpc:String = "https://polygon-mainnet.infura.io/v3/cf04cee8e88e436c9419811c5edbe86c") :
    Parcelable
