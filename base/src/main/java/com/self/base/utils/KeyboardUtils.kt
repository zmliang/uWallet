package com.self.base.utils

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


object KeyboardsUtils {
    /**
     * 显示软键盘
     * @param view
     */
    fun showKeyboard(view: View) {
        val imm: InputMethodManager = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        imm.showSoftInput(view, 0)
    }

    /**
     * 隐藏软键盘
     * @param view
     */
    fun hintKeyBoards(view: View) {
        val manager: InputMethodManager = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 判定当前是否需要隐藏
     */
    fun isShouldHideKeyBord(v: View?, ev: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom: Int = top + v.getHeight()
            val right: Int = left + v.getWidth()
            return !(ev.x>left && ev.x<right && ev.y>top && ev.y<bottom)
        }
        return false
    }
}