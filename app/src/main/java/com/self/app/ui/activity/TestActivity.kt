package com.self.app.ui.activity

import android.content.Intent
import android.content.Intent.FLAG_RECEIVER_FOREGROUND
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.self.app.R
import com.self.app.ex.SettingUtil
import com.self.app.ex.StatusBarUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestActivity:AppCompatActivity() {

    private val flag = 0x30200000
    private val flag_ = FLAG_RECEIVER_FOREGROUND
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setColorNoTranslucent(this, SettingUtil.getColor(this,android.R.color.transparent))
        Log.i("zml","启动：${intent}，isTaskRoot=$isTaskRoot")
        if (!isTaskRoot && intent.action != null) {
            finish()
            return
        }

        setContentView(R.layout.activity_test)

        GlobalScope.launch {
            delay(2000)
            startActivity(Intent(this@TestActivity,ScreenActivity::class.java))
            finish()
        }
    }


}