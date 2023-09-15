package com.self.app.net

import com.self.network.BaseResponse


data class AppResponse<T>(val code: Int, val message: String, val data: T) : BaseResponse<T>() {

    // 这里是示例，wanandroid 网站返回的 错误码为 0 就代表请求成功，请你根据自己的业务需求来改变
    override fun isSuccess() = code == 0

    override fun getResponseCode() = code

    override fun getResponseData() = data

    override fun getResponseMessage() = message

}