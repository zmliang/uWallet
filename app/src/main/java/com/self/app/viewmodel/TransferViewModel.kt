package com.self.app.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.self.app.appViewModel
import com.self.base.BaseViewModel
import com.zml.wallet.WalletAccountMgr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.BigInteger

class TransferViewModel:BaseViewModel() {


    fun transfer(to:String,contract:String,amount:String){
        viewModelScope.launch {
            kotlin.runCatching { withContext(Dispatchers.IO) {

                //BigDecimal("1").scaleByPowerOfTen(18).toBigInteger()

                val curWallet = appViewModel.currentWallet.value
                val privateKey = WalletAccountMgr.get().getPublicChainPrivateKey(curWallet?.walletKey,"")
                WalletAccountMgr.get().transferToken(curWallet?.address ?: "",
                    to,privateKey,contract,
                    BigInteger("1000000000000000000"), appViewModel.curChain.chainId)
            } }
                .onFailure {
                    Log.i("zml","转账失败=$it")

                }

        }
    }


}