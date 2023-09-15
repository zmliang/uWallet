package com.self.app.net


import com.self.app.appViewModel
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class SchemeInterceptor : Interceptor  {
    override fun intercept(chain: Interceptor.Chain): Response {
        //获取request
        //获取request
        val request: Request = chain.request()

        //获取request的创建者builder

        //获取request的创建者builder
        val builder: Request.Builder = request.newBuilder()

        //从request中获取headers，通过给定的键url_name

        //从request中获取headers，通过给定的键url_name
        val headerValues: List<String> = request.headers("base_url_chain_browse")
        return if (headerValues.isNotEmpty()) {
            //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
            builder.removeHeader("base_url_chain_browse")

            //从request中获取原有的HttpUrl实例oldHttpUrl
            val oldHttpUrl: HttpUrl = request.url

            //匹配获得新的BaseUrl
            val headerValue = headerValues[0]
            var newBaseUrl = if ("user" == headerValue) {
                appViewModel.curChain.browserRpc?.toHttpUrlOrNull()
            } else {
                oldHttpUrl
            }

            //重建新的HttpUrl，修改需要修改的url部分
            val newFullUrl = newBaseUrl?.let {
                oldHttpUrl.newBuilder()
                    .scheme(it.scheme)
                    .host(it.host)
                    .port(it.port)
                    .build()
            }

            newFullUrl?.let {
                chain.proceed(builder.url(it).build())
            }
            chain.proceed(request)
        } else {
            chain.proceed(request)
        }
    }
}