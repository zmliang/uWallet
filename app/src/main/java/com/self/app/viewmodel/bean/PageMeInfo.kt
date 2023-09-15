package com.self.app.viewmodel.bean

import android.os.Parcelable
import com.self.app.R
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PageMeInfo(
    var name:String = "Unknown",
    var id:Int = 0,
    var icon:String="msg",
    ) : Parcelable


@Parcelize
data class PageMe(
    var items:MutableList<PageMeInfo>,
    var title:String = ""
) : Parcelable


