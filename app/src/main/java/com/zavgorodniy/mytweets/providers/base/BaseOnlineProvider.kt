package com.zavgorodniy.mytweets.providers.base

import com.zavgorodniy.mytweets.models.base.Model

abstract class BaseOnlineProvider<M : Model<T>, T, NetworkModule> : Provider<M, T> {

    val networkModule: NetworkModule = this.initNetworkModule()

    protected abstract fun initNetworkModule(): NetworkModule

    abstract fun initNewModel(): M
}