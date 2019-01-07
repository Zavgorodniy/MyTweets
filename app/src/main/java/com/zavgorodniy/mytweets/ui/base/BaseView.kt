package com.zavgorodniy.mytweets.ui.base

interface BaseView {

    fun onError(error: Any)

    fun showProgress()

    fun showProgress(isShow: Boolean)

    fun hideProgress()
}