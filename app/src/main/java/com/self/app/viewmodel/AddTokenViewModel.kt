package com.self.app.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.self.app.appViewModel
import com.self.base.BaseViewModel
import com.zml.wallet.TokenInfo
import com.zml.wallet.WalletAccountMgr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddTokenViewModel:BaseViewModel() {

    var tokenInfo = UnPeekLiveData<TokenInfo>()


    fun addToken(contract:String){
        viewModelScope.launch {
            kotlin.runCatching { withContext(Dispatchers.IO) {
                Log.i("zml","currentWallet=${appViewModel.currentWallet.value}")
                appViewModel.currentWallet.value?.address?.let {
                    WalletAccountMgr.get().addCustomCoin(contract, it, appViewModel.curChain)
                        .let { token->
                            run {
                                tokenInfo.postValue(token)
                            }
                        }
                }
            } }
                .onFailure {
                    Log.i("zml","添加新代币throw=$it")

                }
        }
    }
}