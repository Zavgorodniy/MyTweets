package com.zavgorodniy.mytweets.ui.screen.auth.login

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.zavgorodniy.mytweets.models.UserSession
import com.zavgorodniy.mytweets.providers.ProviderInjector
import com.zavgorodniy.mytweets.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class LoginViewModel(app: Application) : BaseViewModel(app) {

    val tokenLiveData = MutableLiveData<UserSession>()

    private val tweetsProvider by lazy { ProviderInjector.getTweetsProvider() }

    private val tokenConsumer =
        Consumer<UserSession> { it -> it?.let { tokenLiveData.value = it } }

    fun signIn() {
        disposable.add(tweetsProvider.signIn()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { isLoadingLiveData.value = false }
            .subscribe(tokenConsumer, onErrorConsumer))
    }
}
