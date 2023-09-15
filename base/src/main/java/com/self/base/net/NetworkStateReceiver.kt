package com.self.base.net

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetworkStateReceiver :BroadcastReceiver() {
    var isInit:Boolean=true
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            if(!isInit){
                if (!NetworkUtil.isNetworkAvailable(context)) {
                    //收到没有网络时判断之前的值是不是有网络，如果有网络才提示通知 ，防止重复通知
                    NetStateManager.instance.mNetworkStateCallback.value?.let {
                        if(it.isConnected){
                            //没网
                            NetStateManager.instance.mNetworkStateCallback.value = NetState(isConnected = false)
                        }
                        return
                    }
                    NetStateManager.instance.mNetworkStateCallback.value = NetState(isConnected = false)
                }else{
                    //收到有网络时判断之前的值是不是没有网络，如果没有网络才提示通知 ，防止重复通知
                    NetStateManager.instance.mNetworkStateCallback.value?.let {
                        if(!it.isConnected){
                            //有网络了
                            NetStateManager.instance.mNetworkStateCallback.value = NetState(isConnected = true)
                        }
                        return
                    }
                    NetStateManager.instance.mNetworkStateCallback.value = NetState(isConnected = true)
                }
            }
            isInit = false
        }
    }
}