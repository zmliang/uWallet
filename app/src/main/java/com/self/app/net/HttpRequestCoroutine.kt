package com.self.app.net

import com.self.app.ex.CacheUtil
import com.self.app.viewmodel.bean.TokenTransEvent
import com.zml.wallet.Chain
import com.zml.wallet.TokenInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext


val HttpRequestCoroutine: HttpRequestManger by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    HttpRequestManger()
}

class HttpRequestManger {


    suspend fun getTokenTransferRecord(contract: String,from:String,page: Int,offset: Int,apiKey:String):
            ChainRpcResponse<ArrayList<TokenTransEvent>>{
        val result =apiService.getTokenTransferRecord(
            module = "account",
            action = "tokentx",
            contractAddress = contract,
            from = from,
            page = page,
            offset = offset,
            key = "A",
            sort = "desc",
            apikey = apiKey
        )

        return result
    }



    suspend fun getHomeData(pageNo: Int): AppResponse<PageResponse<ArrayList<TokenInfo>>> {
        //同时异步请求2个接口，请求完成后合并数据
        return withContext(Dispatchers.IO) {
            val listData = async { apiService.getAritrilList(pageNo) }

            listData.await().data.datas = arrayListOf()

            //如果App配置打开了首页请求置顶文章，且是第一页
            if (CacheUtil.isNeedTop() && pageNo == 0) {
                val topData = async { apiService.getTopAritrilList() }
                listData.await().data.datas.addAll(0, topData.await().data)
                listData.await()
            } else {
                listData.await()
            }
        }
    }




}