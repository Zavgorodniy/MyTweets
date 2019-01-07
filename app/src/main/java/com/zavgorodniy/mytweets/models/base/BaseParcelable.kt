package com.zavgorodniy.mytweets.models.base

import android.os.Parcel
import android.os.Parcelable

interface BaseParcelable : Parcelable {
    override fun describeContents(): Int = 0

    companion object {
        fun <T> generateCreator(create: (source: Parcel) -> T): Parcelable.Creator<T> = object : Parcelable.Creator<T> {
            override fun createFromParcel(source: Parcel): T = create(source)

            override fun newArray(size: Int): Array<out T?> = arrayOfNulls<Any>(size) as Array<T>
        }
    }
}

inline fun <reified T> Parcel.read(): T = readValue(T::class.javaClass.classLoader) as T
fun Parcel.write(vararg values: Any?) = values.forEach { writeValue(it) }