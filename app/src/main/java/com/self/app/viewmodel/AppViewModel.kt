package com.self.app.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.self.app.appViewModel
import com.self.app.ex.SettingUtil
import com.self.app.viewmodel.bean.UIStateResponse
import com.self.app.viewmodel.bean.UserInfo
import com.self.base.BaseViewModel
import com.self.base.appContext
import com.zml.wallet.Chain
import com.zml.wallet.TokenInfo
import com.zml.wallet.WalletAccountMgr
import com.zml.wallet.WalletInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

class AppViewModel : BaseViewModel() {

    lateinit var  allChains :List<Chain>

    lateinit var  curChain:Chain  //钱包当前所属链,polygon 测试链


    val currentWallet = UnPeekLiveData<WalletInfo>()

    fun initChainAndWallet(){
        viewModelScope.launch {
            kotlin.runCatching { withContext(Dispatchers.IO) {
                allChains = appContext.assets.open("chain_config.json").run {
                    Log.i("zml","读取链配置信息")
                    val type: Type = object : TypeToken<List<Chain?>?>() {}.type
                    GsonBuilder().create().fromJson(InputStreamReader(this,StandardCharsets.UTF_8),type)
                }
                curChain = allChains[6]//默认polygon测试链
            } }.onFailure {
                Log.i("zml","initChainAndWallet=$it")
            }
        }
    }

    //App主题颜色 中大型项目不推荐以这种方式改变主题颜色，比较繁琐耦合，且容易有遗漏某些控件没有设置主题色
    var appColor = UnPeekLiveData<Int>()

    //App 列表动画
    var appAnimation = UnPeekLiveData<Int>()

    init {
        initChainAndWallet()
        //默认值颜色
        appColor.value = SettingUtil.getColor(appContext)
        //初始化列表动画
        appAnimation.value = SettingUtil.getListMode()
    }
}