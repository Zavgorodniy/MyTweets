package com.zavgorodniy.mytweets

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.twitter.sdk.android.core.*
import com.zavgorodniy.mytweets.BuildConfig.PREF_NAME
import com.zavgorodniy.mytweets.models.UserSession

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
        lateinit var prefs: SharedPreferences
    }

    private var currentSession: UserSession = UserSession

    override fun onCreate() {
        super.onCreate()
        instance = this
        prefs = getSharedPreferences()
        initTwitter()
    }

    private fun getSharedPreferences() = instance
        .applicationContext
        .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private fun initTwitter() {
        val config = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(
                TwitterAuthConfig(
                    BuildConfig.TWITTER_CONSUMER_KEY,
                    BuildConfig.TWITTER_CONSUMER_SECRET
                )
            )
            .debug(true)
            .build()

        Twitter.initialize(config)
    }

    fun setCurrentSession(session: UserSession?) {
        session?.let {
            currentSession = it
        }
    }

    fun getCurrentSession() = currentSession

    fun onLogout() {
    }
}