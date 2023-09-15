package com.self.app.viewmodel

import androidx.lifecycle.viewModelScope
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.self.app.appViewModel
import com.self.base.BaseViewModel
import com.zml.wallet.WalletAccountMgr
import com.zml.wallet.WalletInfo
import kotlinx.coroutines.launch

class AddWalletViewModel:BaseViewModel() {

    var walletInfo = UnPeekLiveData<WalletInfo>()


    fun createWallet(type:Int,content:String,walletName:String){

        when(type){
            0->{//创建新钱包
                viewModelScope.launch {
                    WalletAccountMgr.get().createWallet(walletName, appViewModel.curChain)?.let {
                        walletInfo.postValue(it)
                    }
                }
            }
            1->{//导入私钥
                viewModelScope.launch {
                    WalletAccountMgr.get().importPrivateKey(content, appViewModel.curChain,walletName)?.let {
                        walletInfo.postValue(it)
                    }
                }
            }
            2->{//导入助记词
                viewModelScope.launch {
                    WalletAccountMgr.get().importMnemonic(content,appViewModel.curChain,walletName)?.let {
                        walletInfo.postValue(it)
                    }
                }

            }
        }

    }

}