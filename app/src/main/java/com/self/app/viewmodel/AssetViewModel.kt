package com.self.app.viewmodel

import androidx.lifecycle.MutableLiveData
import com.self.app.net.HttpRequestCoroutine
import com.self.app.viewmodel.bean.UIStateResponse
import com.self.base.BaseViewModel
import com.self.network.ex.request
import com.zml.wallet.TOKEN_HEADER
import com.zml.wallet.TokenInfo

class AssetViewModel:BaseViewModel() {

    var pageNo = 0

    var tokenInfoDataState: MutableLiveData<UIStateResponse<TokenInfo>> = MutableLiveData()


    /**
     * 获取首页文章列表数据
     * @param isRefresh 是否是刷新，即第一页
     */
    fun reqTokenInfo(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }
        request({ HttpRequestCoroutine.getHomeData(pageNo) }, {
            //请求成功
            pageNo++
            val response = UIStateResponse(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            response.listData.add(0, TOKEN_HEADER)

            //response.listData.add(10,TokenInfo(isHeader = true))
            tokenInfoDataState.value = response
        }, {
            //请求失败
            val listDataUiState =
                UIStateResponse(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<TokenInfo>()
                )
            tokenInfoDataState.value = listDataUiState
        })
    }


}