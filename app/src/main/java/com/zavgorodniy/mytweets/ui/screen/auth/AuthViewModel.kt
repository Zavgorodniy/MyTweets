package com.zavgorodniy.mytweets.ui.screen.auth

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.zavgorodniy.mytweets.models.UserSession
import com.zavgorodniy.mytweets.ui.base.BaseViewModel

class AuthViewModel(application: Application) : BaseViewModel(application) {

    val authLiveData = MutableLiveData<UserSession>()

}