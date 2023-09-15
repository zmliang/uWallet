package com.self.app.viewmodel

import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.self.base.BaseViewModel
import com.zml.wallet.TokenInfo
import com.zml.wallet.WalletInfo

class EventViewModel : BaseViewModel() {


    val updateCurWalletEvent = UnPeekLiveData<WalletInfo>()

    val deleteWalletEvent = UnPeekLiveData<Boolean>()

    val updateWalletNameEvent = UnPeekLiveData<String>()

    val addTokenEvent =  UnPeekLiveData<TokenInfo>()

    val deleteTokenEvent =  UnPeekLiveData<TokenInfo>()

}