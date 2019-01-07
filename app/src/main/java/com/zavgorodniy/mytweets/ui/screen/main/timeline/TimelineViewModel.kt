package com.zavgorodniy.mytweets.ui.screen.main.timeline

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import com.zavgorodniy.mytweets.PAGE_LIMIT
import com.zavgorodniy.mytweets.models.base.TweetWrapper
import com.zavgorodniy.mytweets.providers.ProviderInjector
import com.zavgorodniy.mytweets.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class TimelineViewModel(app: Application) : BaseViewModel(app) {

    val initLiveData = MutableLiveData<List<TweetWrapper>>()
    val moreLiveData = MutableLiveData<List<TweetWrapper>>()

    private val tweetsProvider by lazy { ProviderInjector.getTweetsProvider() }

    private val initConsumer =
        Consumer<List<TweetWrapper>> { it?.takeIf { it.isNotEmpty() }?.let { initLiveData.value = it } }

    private val moreConsumer =
        Consumer<List<TweetWrapper>> { it?.takeIf { it.isNotEmpty() }?.let { moreLiveData.value = it } }

//    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadInit() {
        isLoadingLiveData.value = true
        disposable.add(tweetsProvider.getInitTweets(PAGE_LIMIT)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { isLoadingLiveData.value = false }
            .subscribe(initConsumer, onErrorConsumer))
    }

    fun loadMore(maxId: Long) {
        isLoadingLiveData.value = true
        disposable.add(tweetsProvider.getMoreTweets(maxId, PAGE_LIMIT)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { isLoadingLiveData.value = false }
            .subscribe(initConsumer, onErrorConsumer))
    }
}