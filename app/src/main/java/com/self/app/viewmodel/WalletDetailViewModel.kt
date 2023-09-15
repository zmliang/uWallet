package com.self.app.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.self.app.appViewModel
import com.self.app.eventViewModel
import com.self.base.BaseViewModel
import com.zml.wallet.WalletAccountMgr
import com.zml.wallet.WalletInfo
import kotlinx.coroutines.launch

class WalletDetailViewModel:BaseViewModel() {

    val privateKey: UnPeekLiveData<String> = UnPeekLiveData<String>()

    val mnemonic: UnPeekLiveData<String> = UnPeekLiveData<String>()

    val isDeleted:UnPeekLiveData<Boolean> = UnPeekLiveData<Boolean>()

    fun queryMnemonic(walletKey:String){
        viewModelScope.launch {
            WalletAccountMgr.get().getMnemonic(walletKey,"").let {
                mnemonic.postValue(it)
            }
        }
    }


    fun queryPrivateKey(walletKey:String){
        viewModelScope.launch {
            WalletAccountMgr.get().getPublicChainPrivateKey(walletKey,"").let {
                privateKey.postValue(it)
            }
        }
    }

    fun deleteWallet(walletInfo: WalletInfo){
        viewModelScope.launch {
            WalletAccountMgr.get().deleteWallet(walletInfo).let {
                Log.i("zml","删除钱包成功了吗？$it")
                isDeleted.postValue(it)
            }
        }
    }

    fun updateName(walletInfo: WalletInfo){
        viewModelScope.launch {
            WalletAccountMgr.get().updateWallet(walletInfo).let {
                ToastUtils.showShort(if(it>0) "更新成功" else "更新失败" )
                if (it>0){
                    eventViewModel.updateWalletNameEvent.postValue(walletInfo.walletName)
                }
            }
        }
    }

}