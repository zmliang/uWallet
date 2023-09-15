package com.self.app.net

import com.self.app.ex.CacheUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException



//class AppHeaderInterceptor : Interceptor {
//
//    @Throws(IOException::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
////        val builder = chain.request().newBuilder()
////        builder.addHeader("token", "token123456").build()
////        builder.addHeader("device", "Android").build()
////        builder.addHeader("isLogin", CacheUtil.isLogin().toString()).build()
//        return chain.proceed(builder.build())
//    }
//
//}