package com.zavgorodniy.mytweets.models.base

import android.os.Parcel

interface TweetWrapper : Model<Long> {
    var text: String?
}

class TweetWrapperModel(override var id: Long? = null,
                   override var text: String? = null) : TweetWrapper {
    companion object {
        @JvmField
        val CREATOR = BaseParcelable.generateCreator {
            TweetWrapperModel(it.read(), it.read())
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = dest.write(id, text)
}