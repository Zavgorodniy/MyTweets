package com.zavgorodniy.mytweets.models.converters

import io.reactivex.Flowable

abstract class BaseConverter<IN, OUT> : Converter<IN, OUT> {

    override fun convertInToOut(inObject: IN): OUT = processConvertInToOut(inObject)

    override fun convertListInToOut(inObjects: List<IN>?): List<OUT> =
            inObjects?.let {
                arrayListOf<OUT>().apply {
                    it.forEach {
                        convertInToOut(it)?.let {
                            add(it)
                        }
                    }
                }
            }?: listOf()

    override fun convertInToOutRx(inObject: IN): Flowable<OUT> =
            Flowable.just(convertInToOut(inObject))

    override fun convertListInToOutRx(inObjects: List<IN>): Flowable<List<OUT>> =
            Flowable.just(convertListInToOut(inObjects))

    protected abstract fun processConvertInToOut(inObject: IN): OUT
}