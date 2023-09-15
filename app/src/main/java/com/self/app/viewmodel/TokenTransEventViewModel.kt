package com.self.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils

import com.self.app.appViewModel
import com.self.app.formatDate
import com.self.app.net.HttpRequestCoroutine
import com.self.app.viewmodel.bean.TokenTransEvent
import com.self.app.viewmodel.bean.UIStateResponse
import com.self.base.BaseViewModel
import com.zml.wallet.TokenInfo
import com.zml.wallet.web3.DecimalFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.BigInteger
import java.time.format.DateTimeFormatter

class TokenTransEventViewModel:BaseViewModel() {
    companion object{
        val PAGE_COUNT = 10
    }

    lateinit var curToken:TokenInfo

    val tokenTrans: MutableLiveData<UIStateResponse<TokenTransEvent>> = MutableLiveData()

    private var page = 0


    fun requestTokenTrans(isReload:Boolean){
        val from = appViewModel.currentWallet.value?.address?:""
        val apiKey = appViewModel.curChain.browserKey?:""

        viewModelScope.launch {
            showLoading.postValue("")
            if (isReload){
                page = 0
            }
            kotlin.runCatching { withContext(Dispatchers.IO) {
                val ret = HttpRequestCoroutine.getTokenTransferRecord(
                    contract = curToken.contract,
                    from = from,
                    page = page,
                    offset = PAGE_COUNT,
                    apiKey = apiKey)
                dismissLoading.postValue(true)

                val listDatas = ret.getResponseData()
                page++
                val response = UIStateResponse(
                    isSuccess = true,
                    errMessage = "",
                    isRefresh = isReload,
                    isEmpty = listDatas.isEmpty(),
                    hasMore = listDatas.size != 0,
                    listData = listDatas.apply {
                        for (item in this){//在这里计算好要显示的内容
                            calc(item)
                        }
                    }
                )
                tokenTrans.postValue(response)

            } }.onFailure {
                dismissLoading.postValue(true)
                Log.i("zml","请求token出错=$it")
            }
        }
    }

    private fun calc(tokenTransEvent: TokenTransEvent){
        val decimal = tokenTransEvent.tokenDecimal
        val value = DecimalFormat.div(BigInteger(tokenTransEvent.value),decimal.toInt(),2)
        val gasFee = BigDecimal(tokenTransEvent.gasPrice).multiply(BigDecimal(tokenTransEvent.gasUsed))
        val gf_value = DecimalFormat.div(gasFee.toBigInteger(),decimal.toInt(),8)
        val timeStamp = tokenTransEvent.timeStamp.formatDate()

        tokenTransEvent.value = value
        tokenTransEvent.gas = gf_value
        tokenTransEvent.timeStamp = timeStamp
        tokenTransEvent.isIncome = tokenTransEvent.to.lowercase() == appViewModel.currentWallet.value?.address?.lowercase()
    }
}