package com.self.app.viewmodel.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BannerResponse(
    var desc: String = "",
    var id: Int = 0,
    var imagePath: String = "",
    var isVisible: Int = 0,
    var order: Int = 0,
    var title: String = "",
    var type: Int = 0,
    var url: String = ""
) : Parcelable