package com.zavgorodniy.mytweets.ui.screen.main.timeline

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.zavgorodniy.mytweets.R
import com.zavgorodniy.mytweets.ui.base.BaseFragment
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter
import com.twitter.sdk.android.tweetui.UserTimeline
import com.zavgorodniy.mytweets.App
import com.zavgorodniy.mytweets.PAGE_LIMIT
import kotlinx.android.synthetic.main.fragment_timeline.*

class TimelineFragment : BaseFragment<TimelineViewModel>() {

    companion object {
        fun newInstance() = TimelineFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    override val viewModelClass = TimelineViewModel::class.java

    override val layoutId = R.layout.fragment_timeline

    private var timelineCallback: TimelineCallback? = null

    private val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        timelineCallback = bindInterface<TimelineCallback>(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvTimeline.layoutManager = layoutManager
        srTimeline.setOnRefreshListener { getTimeline() }
        getTimeline()
    }

    override fun onDetach() {
        timelineCallback = null
        super.onDetach()
    }

    override fun observeLiveData() {
        // no need
    }

    private fun getTimeline() {
        srTimeline.isRefreshing = true
        val userTimeline = UserTimeline.Builder()
            .includeReplies(true)
            .includeRetweets(true)
            .maxItemsPerRequest(PAGE_LIMIT)
            .userId(App.instance.getCurrentSession().userId)
            .build()

        rvTimeline.adapter = TweetTimelineRecyclerViewAdapter.Builder(context)
            .setTimeline(userTimeline)
            .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
            .build()

        srTimeline.isRefreshing = false
    }
}
