package com.self.base.net

import com.kunminx.architecture.ui.callback.UnPeekLiveData

class NetStateManager private constructor(){
    val mNetworkStateCallback = UnPeekLiveData<NetState>()

    companion object {
        val instance: NetStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetStateManager()
        }
    }
}