package com.zavgorodniy.mytweets.ui.base

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import com.twitter.sdk.android.core.TwitterException
import com.zavgorodniy.mytweets.R
import com.zavgorodniy.mytweets.extensions.hide
import com.zavgorodniy.mytweets.extensions.show
import com.zavgorodniy.mytweets.network.error.AppError
import kotlinx.android.synthetic.main.include_progress.*

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(),
    BaseView {

    protected abstract val viewModelClass: Class<T>

    protected abstract val containerId: Int

    protected abstract val layoutId: Int

    protected abstract fun observeLiveData()

    protected open fun hasProgressBar() = false

    private var progressView: FrameLayout? = null

    protected val viewModel: T by lazy {
        ViewModelProviders.of(this).get(viewModelClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        if (hasProgressBar()) {
            progressView = flProgressView
        }
        observeAllData()
    }

    private fun observeAllData() {
        observeLiveData()
        with(viewModel) {
            isLoadingLiveData.observe(this@BaseActivity, Observer<Boolean> { it?.let { showProgress(it) } })
            errorLiveData.observe(this@BaseActivity, Observer<Any> { it?.let { onError(it) } })
        }
    }

    protected fun replaceFragment(fragment: Fragment, needToAddToBackStack: Boolean = true) {
        val name = fragment.javaClass.simpleName
        supportFragmentManager.beginTransaction().apply {
            replace(containerId, fragment, name)
            if (needToAddToBackStack) addToBackStack(name)
        }.commitAllowingStateLoss()
    }

    override fun onError(error: Any) {
        hideProgress()
        when (error) {
            is String -> showAlert(title = getString(R.string.error_title), message = error)
            is AppError -> showAlert(title = error.title, message = error.message)
            is TwitterException -> showAlert(title = getString(R.string.error_title), message = error.message)
        }
    }

    fun showAlert(
        message: String?,
        title: String? = null,
        cancelable: Boolean = true,
        positiveRes: Int = R.string.ok,
        positiveFun: () -> Unit = {},
        negativeRes: Int? = null,
        negativeFun: () -> Unit = {}
    ) {
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setCancelable(cancelable)
            title?.let { setTitle(it) }
            setPositiveButton(positiveRes) { _, _ -> positiveFun() }
            negativeRes?.let { setNegativeButton(it) { _, _ -> negativeFun() } }
            show()
        }
    }

    override fun showProgress() {
        progressView?.show()
    }

    override fun showProgress(isShow: Boolean) {
        if (isShow) showProgress() else hideProgress()
    }

    override fun hideProgress() {
        progressView?.hide(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.findFragmentById(containerId)?.onActivityResult(requestCode, resultCode, data)
    }
}