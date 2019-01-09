package com.zavgorodniy.mytweets.ui.screen.auth.login

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.zavgorodniy.mytweets.App
import com.zavgorodniy.mytweets.R
import com.zavgorodniy.mytweets.models.UserSession
import com.zavgorodniy.mytweets.ui.base.BaseFragment
import com.zavgorodniy.mytweets.utils.TokenType
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment<LoginViewModel>() {

    companion object {
        fun newInstance() = LoginFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    override val viewModelClass = LoginViewModel::class.java

    override val layoutId = R.layout.fragment_login

    private var loginCallback: LoginCallback? = null

    private val twitterCallback = object : Callback<TwitterSession>() {
        override fun success(result: Result<TwitterSession>?) {
            result?.data?.let {
                UserSession.newSession(
                    it.userId,
                    it.userName,
                    it.authToken?.secret,
                    it.authToken?.token,
                    TokenType.TYPE_AUTH()
                )
                App.instance.setCurrentSession(UserSession)
                loginCallback?.onUserLogin()
            }
        }

        override fun failure(exception: TwitterException?) {
            exception?.let { onError(it) }
        }
    }

    private val tokenObserver = Observer<UserSession> { it ->
        it?.let {
            it.userName = etUserName.text.toString()
            loginCallback?.onUserLogin()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        loginCallback = bindInterface<LoginCallback>(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btLogin.callback = twitterCallback
        btConfirmName.setOnClickListener { viewModel.signIn() }
    }

    override fun onDetach() {
        loginCallback = null
        super.onDetach()
    }

    override fun observeLiveData() {
        viewModel.tokenLiveData.observe(this@LoginFragment, tokenObserver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        btLogin.onActivityResult(requestCode, resultCode, data)
    }
}
