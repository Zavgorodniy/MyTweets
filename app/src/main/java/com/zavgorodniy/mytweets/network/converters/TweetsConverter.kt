package com.zavgorodniy.mytweets.network.converters

import com.twitter.sdk.android.core.models.Tweet
import com.zavgorodniy.mytweets.models.base.TweetWrapper
import com.zavgorodniy.mytweets.models.base.TweetWrapperModel
import com.zavgorodniy.mytweets.models.converters.BaseConverter

interface TweetConverter

class TweetConverterImpl : BaseConverter<Tweet, TweetWrapper>(), TweetConverter {

    override fun processConvertInToOut(inObject: Tweet) = inObject.run {
        TweetWrapperModel(text = text)
    }
}