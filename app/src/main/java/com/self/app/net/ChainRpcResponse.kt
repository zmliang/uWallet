package com.self.app.net

import com.self.network.BaseResponse


data class ChainRpcResponse<T>(val status: Int, val message: String, val result: T) : BaseResponse<T>() {

    // 这里是示例，wanandroid 网站返回的 错误码为 0 就代表请求成功，请你根据自己的业务需求来改变
    override fun isSuccess() = status == 0

    override fun getResponseCode() = status

    override fun getResponseData() = result

    override fun getResponseMessage() = message

}