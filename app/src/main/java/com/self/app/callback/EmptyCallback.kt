package com.self.app.callback

import com.kingja.loadsir.callback.Callback
import com.self.app.R


class EmptyCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_empty
    }
}