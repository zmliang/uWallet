package com.self.app.ex

import android.text.TextUtils
import com.google.gson.Gson
import com.self.app.viewmodel.bean.UserInfo
import com.tencent.mmkv.MMKV

object CacheUtil {
    private const val MM_ID :String = "app"


    fun getCurSelectedChain():Int {
        val kv = MMKV.mmkvWithID(MM_ID)
        return kv.decodeInt("cur_selected_chain_id") ?: 0x89
    }

    fun setCurSelectedChain(chainId:Int){
        val kv = MMKV.mmkvWithID(MM_ID)
        kv.encode("cur_selected_chain_id",chainId?:0x89)
    }



    fun isLogin(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("login", false)
    }


    /**
     * 首页是否开启获取指定文章
     */
    fun isNeedTop(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("top", true)
    }
    /**
     * 设置首页是否开启获取指定文章
     */
    fun setIsNeedTop(isNeedTop:Boolean): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.encode("top", isNeedTop)
    }

    fun getCurChainSelectedWallet(chainId: String):Int{
        val kv = MMKV.mmkvWithID(MM_ID)
        return kv.decodeInt("cur_"+chainId+"_chain_wallet") ?: 0
    }

    fun autoGen(chainId: String):Int{
        var key = chainId
        if (key.isNullOrEmpty()){
            key = "my_local_chain"
        }

        val kv = MMKV.mmkvWithID(MM_ID)
        var index = kv.decodeInt(key)
        if (index == null){
            index = 0
        }
        kv.encode(key,index+1)
        return index
    }

}