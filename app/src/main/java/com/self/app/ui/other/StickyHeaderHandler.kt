package com.self.app.ui.other

import android.view.View

interface StickyHeaderHandler {

    companion object {
        const val HEADER_POSITION_NOT_FOUND = -1
    }

    /** StickyHeader的位置 */
    fun getHeaderPosition(itemPosition: Int): Int
    /** 返回StickyHeader的positon */
    fun getHeaderLayout(headerPosition: Int): Int
    /** 绑定数据到StickyHeader */
    fun bindHeaderData(header: View?, headerPosition: Int)
    /** 判断是否是header */
    fun isHeader(itemPosition: Int): Boolean

}