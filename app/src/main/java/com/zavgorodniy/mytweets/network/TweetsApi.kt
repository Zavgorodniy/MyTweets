package com.zavgorodniy.mytweets.network

import com.twitter.sdk.android.core.models.Tweet
import com.zavgorodniy.mytweets.network.requests.TokenRequest
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TweetsApi {

//    @POST("oauth2/token")
//    fun signIn(@Body request: TokenRequest): Flowable<ResponseBean<UserSessionBean>>

    @GET("${NetworkModule.API_VERSION}statuses/home_timeline")
    fun getInitTweets(@Query("count") count: Int): Flowable<List<Tweet>>

    @GET("${NetworkModule.API_VERSION}statuses/home_timeline")
    fun getMoreTweets(@Query("max_id") maxId: Long,
                      @Query("count") count: Int): Flowable<List<Tweet>>
}