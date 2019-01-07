package com.zavgorodniy.mytweets.ui.screen.auth.login

import com.twitter.sdk.android.core.TwitterSession

interface LoginCallback {

    fun onUserLogin(session: TwitterSession)
}