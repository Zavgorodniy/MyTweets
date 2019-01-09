package com.zavgorodniy.mytweets.network.api

import com.twitter.sdk.android.core.models.Tweet
import com.zavgorodniy.mytweets.network.NetworkModule
import com.zavgorodniy.mytweets.network.responses.TokenResponse
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TweetsApi {

    @POST("oauth2/token")
    fun signIn(@Body body: String): Flowable<TokenResponse>

    @GET("${NetworkModule.API_VERSION}statuses/user_timeline.json")
    fun getInitTweets(@Query("screen_name") userName: String,
                      @Query("count") count: Int): Flowable<List<Tweet>>

    @GET("${NetworkModule.API_VERSION}statuses/user_timeline.json")
    fun getMoreTweets(@Query("screen_name") userName: String,
                      @Query("max_id") maxId: Long,
                      @Query("count") count: Int): Flowable<List<Tweet>>
}