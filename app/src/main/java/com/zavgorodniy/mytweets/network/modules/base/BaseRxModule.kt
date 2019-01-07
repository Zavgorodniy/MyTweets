package com.zavgorodniy.mytweets.network.modules.base

import com.zavgorodniy.mytweets.models.converters.Converter

abstract class BaseRxModule<T, NetworkModel, M> {
    protected abstract val api: T

    protected abstract val converter: Converter<NetworkModel, M>
}