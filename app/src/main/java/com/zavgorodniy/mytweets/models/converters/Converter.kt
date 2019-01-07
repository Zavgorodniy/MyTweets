package com.zavgorodniy.mytweets.models.converters

import io.reactivex.Flowable

interface Converter<IN, OUT> {

    fun convertInToOut(inObject: IN): OUT

    fun convertInToOutRx(inObject: IN): Flowable<OUT>

    fun convertListInToOut(inObjects: List<IN>?): List<OUT>?

    fun convertListInToOutRx(inObjects: List<IN>): Flowable<List<OUT>>?
}