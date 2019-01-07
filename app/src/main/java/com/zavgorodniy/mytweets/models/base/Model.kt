package com.zavgorodniy.mytweets.models.base

interface Model<T> : BaseParcelable {

    var id: T?
}