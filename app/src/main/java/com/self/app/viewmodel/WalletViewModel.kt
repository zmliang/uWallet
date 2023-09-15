package com.self.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.self.app.appViewModel
import com.self.app.eventViewModel
import com.self.app.ex.CacheUtil
import com.self.app.viewmodel.bean.UIStateResponse
import com.self.base.BaseViewModel
import com.self.base.appContext
import com.zml.wallet.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

class WalletViewModel: BaseViewModel() {


    var walletAccounts:  MutableLiveData<UIStateResponse<WalletInfo>> = MutableLiveData()//当前链显示的所有钱包

    var tokenData: MutableLiveData<UIStateResponse<TokenInfo>> = MutableLiveData()


    fun requestWalletInfo(isRefresh:Boolean){
        viewModelScope.launch {
            WalletAccountMgr.get().queryWalletByChain(appViewModel.curChain).let {
                val response = UIStateResponse(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = false,
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it)
                if (it.isEmpty()){
                    it.add(0,WALLET_PLACEHOLDER)
                }
                walletAccounts.postValue(response)

                var walletPos = appViewModel.curChain.chainId.let { it1 ->
                    CacheUtil.getCurChainSelectedWallet(
                        it1
                    )
                } ?:0
                if (walletPos>it.size){
                    walletPos = 0
                }
                appViewModel.currentWallet.postValue(it.get(walletPos))
            }
        }
    }

    fun requestTokenInfo(isRefresh:Boolean,walletInfo: WalletInfo){

        viewModelScope.launch {
            kotlin.runCatching { withContext(Dispatchers.IO) {
                WalletAccountMgr.get().queryChainTokens(appViewModel.curChain,walletInfo).let { datas->
                    Log.i("zml","data=$datas")
                    val response = UIStateResponse(
                        isSuccess = true,
                        isRefresh = isRefresh,
                        isEmpty = datas.isEmpty(),
                        hasMore = false,
                        isFirstEmpty = isRefresh && datas.isEmpty(),
                        listData = datas)
                    if (isRefresh){
                        datas.add(0, TOKEN_HEADER)
                    }
                   // datas.add(TokenInfo(symbol = "BTC", decimal = "0", name = "BTC", contract = ""))
                    tokenData.postValue(response)
                }
            } }.onFailure {
                Log.i("zml","请求token出错=$it")
            }

        }
    }

    fun requestDeleteToken(tokenInfo: TokenInfo,pos:Int){
        viewModelScope.launch {
            kotlin.runCatching { withContext(Dispatchers.IO) {
                WalletAccountMgr.get().deleteToken(tokenInfo, appViewModel.curChain).let { result->
                    if (result>0){
                        eventViewModel.deleteTokenEvent.postValue(tokenInfo)
                    }else{
                        ToastUtils.showShort("删除代币失败")
                    }
                }
            } }.onFailure {
                Log.i("zml","请求token出错=$it")
            }

        }
    }
}