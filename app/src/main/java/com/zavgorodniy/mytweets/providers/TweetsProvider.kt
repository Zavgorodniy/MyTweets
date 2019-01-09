package com.zavgorodniy.mytweets.providers

import android.arch.lifecycle.MutableLiveData
import com.twitter.sdk.android.core.models.Tweet
import com.zavgorodniy.mytweets.models.UserSession
import com.zavgorodniy.mytweets.providers.base.Provider
import com.zavgorodniy.mytweets.providers.base.BaseOnlineProvider
import com.zavgorodniy.mytweets.models.base.TweetWrapper
import com.zavgorodniy.mytweets.models.base.TweetWrapperModel
import com.zavgorodniy.mytweets.network.NetworkModule
import com.zavgorodniy.mytweets.network.modules.TweetsModule
import io.reactivex.Flowable

interface TweetsProvider : Provider<TweetWrapper, Long> {

    val tokenLiveData: MutableLiveData<UserSession>

    fun signIn(): Flowable<UserSession>

    fun getInitTweets(userName: String, count: Int): Flowable<List<Tweet>>

    fun getMoreTweets(userName: String, maxId: Long, count: Int): Flowable<List<Tweet>>
}

object TweetsProviderImpl : BaseOnlineProvider<TweetWrapper, Long, TweetsModule>(),
    TweetsProvider {

    override fun initNetworkModule() = NetworkModule.getTweetsModule()

    override fun initNewModel() = TweetWrapperModel()

    override val tokenLiveData = MutableLiveData<UserSession>()

    override fun signIn(): Flowable<UserSession> =
        networkModule.signIn()
            .doOnNext { tokenLiveData.postValue(it) }

    override fun getInitTweets(userName: String, count: Int) =
        networkModule.getInitTweets(userName, count)

    override fun getMoreTweets(userName: String, maxId: Long, count: Int) =
        networkModule.getMoreTweets(userName, maxId, count)
}