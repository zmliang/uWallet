package com.self.app.viewmodel.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MatchingOrderInfo(
    var name:String = "Polygon",
    var property:Int = 0,
    val isHeader:Boolean=false
    ) : Parcelable
