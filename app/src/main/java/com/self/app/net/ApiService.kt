package com.self.app.net

import com.self.app.viewmodel.bean.BannerResponse
import com.self.app.viewmodel.bean.TokenTransEvent
import com.self.app.viewmodel.bean.UserInfo
import com.zml.wallet.Chain
import com.zml.wallet.TokenInfo
import retrofit2.http.*

interface ApiService {

    companion object {
        const val SERVER_URL = "https://wanandroid.com/"
        const val SERVER_URL1 = "https://wanandroid.com/"
    }

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") pwd: String
    ): AppResponse<UserInfo>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String, @Field("password") pwd: String, @Field(
            "repassword"
        ) rpwd: String
    ): AppResponse<Any>

    /**
     * 获取banner数据
     */
    @GET("banner/json")
    suspend fun getBanner(): AppResponse<ArrayList<BannerResponse>>






    /**
     * 获取置顶文章集合数据
     */
    @GET("article/top/json")
    suspend fun getTopAritrilList(): AppResponse<ArrayList<TokenInfo>>

    /**
     * 获取首页文章数据
     */
    @GET("article/list/{page}/json")
    suspend fun getAritrilList(@Path("page") pageNo: Int): AppResponse<PageResponse<ArrayList<TokenInfo>>>

    /**
     * 添加一个TODO
     */
    @POST("/lg/todo/add/json")
    @FormUrlEncoded
    suspend fun addTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int
    ): AppResponse<Any?>

    /**
     * 修改一个TODO
     */
    @POST("/lg/todo/update/{id}/json")
    @FormUrlEncoded
    suspend fun updateTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int,
        @Path("id") id: Int
    ): AppResponse<Any?>

    /**
     * 删除一个TODO
     */
    @POST("/lg/todo/delete/{id}/json")
    suspend fun deleteTodo(@Path("id") id: Int): AppResponse<Any?>

    /**
     * 完成一个TODO
     */
    @POST("/lg/todo/done/{id}/json")
    @FormUrlEncoded
    suspend fun doneTodo(@Path("id") id: Int, @Field("status") status: Int): AppResponse<Any?>


    //@Headers("base_url_chain_browse")
    @GET("api?")
    suspend fun getTokenTransferRecord(
        @Query("module")module:String,
        @Query("action")action:String,
        @Query("contractaddress")contractAddress:String,
        @Query("address")from:String,
        @Query("page")page:Int,
        @Query("offset")offset:Int,
        @Query("key")key:String,
        @Query("sort")sort:String,
        @Query("apikey")apikey:String,
        ): ChainRpcResponse<ArrayList<TokenTransEvent>>


}