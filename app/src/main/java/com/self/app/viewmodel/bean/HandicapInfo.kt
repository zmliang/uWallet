package com.self.app.viewmodel.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class HandicapInfo(
    var price:Float = 0f,
    var volume:Float = 0f,
    var type:Int = 0,//0-买     1-卖
    val isHeader:Boolean=false
    ) : Parcelable
