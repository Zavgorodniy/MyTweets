package com.zavgorodniy.mytweets.providers

import android.arch.lifecycle.MutableLiveData
import com.zavgorodniy.mytweets.providers.base.Provider
import com.zavgorodniy.mytweets.providers.base.BaseOnlineProvider
import com.zavgorodniy.mytweets.models.base.TweetWrapper
import com.zavgorodniy.mytweets.models.base.TweetWrapperModel
import com.zavgorodniy.mytweets.network.NetworkModule
import com.zavgorodniy.mytweets.network.modules.TweetsModule
import io.reactivex.Flowable

interface TweetsProvider : Provider<TweetWrapper, Long> {

    val tweetsLiveData: MutableLiveData<List<TweetWrapper>>

    fun getInitTweets(count: Int): Flowable<List<TweetWrapper>>

    fun getMoreTweets(maxId: Long, count: Int): Flowable<List<TweetWrapper>>
}

object TweetsProviderImpl : BaseOnlineProvider<TweetWrapper, Long, TweetsModule>(),
    TweetsProvider {

    override fun initNetworkModule() = NetworkModule.getTweetsModule()

    override fun initNewModel() = TweetWrapperModel()

    override val tweetsLiveData = MutableLiveData<List<TweetWrapper>>()

    override fun getInitTweets(count: Int) =
        networkModule.getInitTweets(count)
            .doOnNext { tweetsLiveData.postValue(it) }

    override fun getMoreTweets(maxId: Long, count: Int) =
        networkModule.getMoreTweets(maxId, count)
            .doOnNext { tweetsLiveData.postValue(it) }
}