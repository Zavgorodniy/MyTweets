package com.zavgorodniy.mytweets.ui.screen.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.zavgorodniy.mytweets.R
import com.zavgorodniy.mytweets.ui.base.BaseActivity
import com.zavgorodniy.mytweets.ui.screen.main.timeline.TimelineFragment

class MainActivity : BaseActivity<MainViewModel>() {

    companion object {

        fun start(context: Context) {
            with(context) {
                Intent(this, MainActivity::class.java).run { startActivity(this) }
            }
        }

        fun start(context: Context, intent: Intent) {
            with(intent) {
                setClass(context, MainActivity::class.java).let { context.startActivity(it) }
            }
        }
    }

    override fun hasProgressBar() = true

    override val viewModelClass = MainViewModel::class.java
    override val containerId = R.id.container
    override val layoutId = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openTimeline(false)
    }

    override fun observeLiveData() {
        // no need
    }

    private fun openTimeline(needToAddToBackStack: Boolean) {
        replaceFragment(TimelineFragment.newInstance(), needToAddToBackStack)
    }
}
