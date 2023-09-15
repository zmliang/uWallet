package com.zml.wallet

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//@Parcelize
//data class TokenInfo(var symbol:String?="",
//                    var name:String? ="",
//                    val contract:String,
//                    var decimal:String? = "",
//                    var chainId:String?="",
//                    var balance:String?="0"):Parcelable



@Parcelize
data class TokenInfo(
    var coinIcon:String = "https://download.metatdex.com/administer/pictures/custom_management/20230822020641.png",
    var curPrice:String = "23.1",
    var range:Int = 1,
    var usdtVolume:Float = 0.5f,
    var symbol:String?,
    var decimal:String?,
    var name:String?,
    var contract:String,
    var chainId:String="",
    var balance:String?="0",
    var isFresh:Boolean=true,
    var isSuccess:Boolean=true,
) : Parcelable

val TOKEN_HEADER = TokenInfo(symbol = "HEADER", decimal = "", name = "", contract = "",)