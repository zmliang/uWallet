package com.self.app.callback

import com.kingja.loadsir.callback.Callback
import com.self.app.R

class ErrorCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_error
    }
}