package com.zavgorodniy.mytweets.ui.base

import android.app.Application
import android.arch.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
    LifecycleObserver {

    val errorLiveData = MutableLiveData<Any>()
    val isLoadingLiveData = MediatorLiveData<Boolean>()

    val disposable = CompositeDisposable()

    fun setLoadingLiveData(vararg mutableLiveData: MutableLiveData<*>) {
        mutableLiveData.forEach { liveData ->
            isLoadingLiveData.apply {
                this.removeSource(liveData)
                this.addSource(liveData) { this.value = false }
            }
        }
    }

    val onErrorConsumer = Consumer<Throwable> {
        val errorString = it.message
        errorLiveData.value = if (errorString?.isNotEmpty() == true) errorString else it.message
        hideLoadingProgress()
    }

    protected open fun hideLoadingProgress() {
        isLoadingLiveData.value = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun dispose() {
        disposable.clear()
    }
}