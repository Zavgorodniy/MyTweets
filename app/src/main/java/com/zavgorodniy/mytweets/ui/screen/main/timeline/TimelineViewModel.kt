package com.zavgorodniy.mytweets.ui.screen.main.timeline

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import com.twitter.sdk.android.core.models.Tweet
import com.zavgorodniy.mytweets.App
import com.zavgorodniy.mytweets.PAGE_LIMIT
import com.zavgorodniy.mytweets.providers.ProviderInjector
import com.zavgorodniy.mytweets.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class TimelineViewModel(app: Application) : BaseViewModel(app) {

    val initLiveData = MutableLiveData<List<Tweet>>()
    val moreLiveData = MutableLiveData<List<Tweet>>()

    private val tweetsProvider by lazy { ProviderInjector.getTweetsProvider() }

    private val initConsumer =
        Consumer<List<Tweet>> { it?.takeIf { it.isNotEmpty() }?.let { initLiveData.value = it } }

    private val moreConsumer =
        Consumer<List<Tweet>> { it?.takeIf { it.isNotEmpty() }?.let { moreLiveData.value = it } }

    //    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadInit() {
        App.instance.getCurrentSession().userName.takeUnless { it.isNullOrEmpty() }?.let {
            isLoadingLiveData.value = true
            disposable.add(tweetsProvider.getInitTweets(it, PAGE_LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { isLoadingLiveData.value = false }
                .subscribe(initConsumer, onErrorConsumer))
        }
    }

    fun loadMore(maxId: Long) {
        App.instance.getCurrentSession().userName.takeUnless { it.isNullOrEmpty() }?.let {
            isLoadingLiveData.value = true
            disposable.add(tweetsProvider.getMoreTweets(it, maxId, PAGE_LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { isLoadingLiveData.value = false }
                .subscribe(initConsumer, onErrorConsumer))
        }
    }
}