package com.zavgorodniy.mytweets.network.modules

import com.twitter.sdk.android.core.models.Tweet
import com.zavgorodniy.mytweets.models.UserSession
import com.zavgorodniy.mytweets.models.base.TweetWrapper
import com.zavgorodniy.mytweets.network.NetworkModule.GRANT_TYPE_VALUE
import com.zavgorodniy.mytweets.network.api.TweetsApi
import com.zavgorodniy.mytweets.network.converters.SessionConverterImpl
import com.zavgorodniy.mytweets.network.converters.TweetConverterImpl
import com.zavgorodniy.mytweets.network.modules.base.BaseRxModule
import io.reactivex.Flowable

interface TweetsModule {

    fun signIn(): Flowable<UserSession>

    fun getInitTweets(userName: String, count: Int): Flowable<List<Tweet>>

    fun getMoreTweets(userName: String, maxId: Long, count: Int): Flowable<List<Tweet>>
}

class TweetsModuleImpl(override val api: TweetsApi) : BaseRxModule<TweetsApi, Tweet, TweetWrapper>(), TweetsModule {

    override val converter by lazy { TweetConverterImpl() }
    private val sessionConverter by lazy { SessionConverterImpl() }

    override fun signIn(): Flowable<UserSession> =
        api.signIn(GRANT_TYPE_VALUE)
            .map { sessionConverter.convertInToOut(it) }

    override fun getInitTweets(userName: String, count: Int): Flowable<List<Tweet>> =
        api.getInitTweets(userName, count)

    override fun getMoreTweets(userName: String, maxId: Long, count: Int): Flowable<List<Tweet>> =
        api.getMoreTweets(userName, maxId, count)
}