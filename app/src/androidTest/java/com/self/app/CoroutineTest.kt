package com.self.app

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CoroutineTest {

    fun test_1(){
        GlobalScope.launch {
            Log.i("zml","aaa")
        }
    }


}