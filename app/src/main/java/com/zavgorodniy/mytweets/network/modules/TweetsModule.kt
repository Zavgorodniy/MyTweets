package com.zavgorodniy.mytweets.network.modules

import com.twitter.sdk.android.core.models.Tweet
import com.zavgorodniy.mytweets.models.base.TweetWrapper
import com.zavgorodniy.mytweets.network.TweetsApi
import com.zavgorodniy.mytweets.network.converters.TweetConverterImpl
import com.zavgorodniy.mytweets.network.modules.base.BaseRxModule
import io.reactivex.Flowable

interface TweetsModule {

    fun getInitTweets(count: Int): Flowable<List<TweetWrapper>>

    fun getMoreTweets(maxId: Long, count: Int): Flowable<List<TweetWrapper>>
}

class TweetsModuleImpl(override val api: TweetsApi) : BaseRxModule<TweetsApi, Tweet, TweetWrapper>(), TweetsModule {

    override val converter by lazy { TweetConverterImpl() }

    override fun getInitTweets(count: Int): Flowable<List<TweetWrapper>> =
        api.getInitTweets(count)
            .map { converter.convertListInToOut(it) }

    override fun getMoreTweets(maxId: Long, count: Int): Flowable<List<TweetWrapper>> =
        api.getMoreTweets(maxId, count)
            .map { converter.convertListInToOut(it) }
}