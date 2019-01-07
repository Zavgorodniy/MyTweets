package com.zavgorodniy.mytweets.ui.base

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

abstract class BaseFragment<T : BaseViewModel> : Fragment(),
    BaseView {

    protected var toolbar: Toolbar? = null

    abstract val viewModelClass: Class<T>

    protected val viewModel: T by lazy {
        ViewModelProviders.of(this).get(viewModelClass).apply {
            this@BaseFragment.lifecycle.addObserver(this)
        }
    }

    protected abstract fun observeLiveData()

    protected abstract val layoutId: Int

    private var baseView: BaseView? = null

    private val textWatchers: Map<EditText?, TextWatcher> = mutableMapOf()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        baseView = bindInterface<BaseView>(parentFragment, context)
    }

    override fun onDestroyView() {
        textWatchers.forEach { (key, value) -> key?.removeTextChangedListener(value) }
        super.onDestroyView()
    }

    override fun onDetach() {
        baseView = null
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutId, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAllData()
    }

    private fun observeAllData() {
        observeLiveData()
        with(viewModel) {
            isLoadingLiveData.observe(this@BaseFragment, Observer<Boolean> { it?.let { showProgress(it) } })
            errorLiveData.observe(this@BaseFragment, Observer<Any> {
                it?.let {
                    onError(it)
                    errorLiveData.value = null
                }
            })
        }
    }

    override fun showProgress() {
        baseView?.showProgress()
    }

    override fun hideProgress() {
        baseView?.hideProgress()
    }

    override fun showProgress(isShow: Boolean) {
        if (isShow) showProgress() else hideProgress()
    }

    override fun onError(error: Any) {
        baseView?.onError(error)
    }

    protected inline fun <reified T> bindInterface(vararg objects: Any?):
            T? = objects.find { it is T }?.let { it as T }
}