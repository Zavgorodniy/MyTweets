package com.zavgorodniy.mytweets.ui.screen.auth

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.twitter.sdk.android.core.TwitterSession
import com.zavgorodniy.mytweets.ui.screen.auth.login.LoginFragment
import com.zavgorodniy.mytweets.R
import com.zavgorodniy.mytweets.models.UserSession
import com.zavgorodniy.mytweets.ui.base.BaseActivity
import com.zavgorodniy.mytweets.ui.screen.auth.login.LoginCallback
import com.zavgorodniy.mytweets.ui.screen.main.MainActivity

class AuthActivity : BaseActivity<AuthViewModel>(),
    LoginCallback {

    companion object {
        fun start(context: Context) {
            with(context) {
                Intent(this, AuthActivity::class.java).run { startActivity(this) }
            }
        }

        fun start(context: Context, intent: Intent) {
            with(intent) {
                setClass(context, AuthActivity::class.java).let { context.startActivity(it) }
            }
        }
    }

    override fun hasProgressBar() = true

    override val viewModelClass = AuthViewModel::class.java
    override val containerId = R.id.container
    override val layoutId = R.layout.activity_auth

    private val authObserver = Observer<UserSession> {
        MainActivity.start(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openLogin(false)
    }

    override fun observeLiveData() {
        with(viewModel) {
            authLiveData.observe(this@AuthActivity, authObserver)
        }
    }

    private fun openLogin(needToAddToBackStack: Boolean) {
        replaceFragment(LoginFragment.newInstance(), needToAddToBackStack)
    }

    override fun onUserLogin(session: TwitterSession) {
        MainActivity.start(this)
    }
}
