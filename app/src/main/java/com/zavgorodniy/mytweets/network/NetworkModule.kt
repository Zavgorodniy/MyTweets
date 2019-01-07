package com.zavgorodniy.mytweets.network

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.zavgorodniy.mytweets.App
import com.zavgorodniy.mytweets.BuildConfig
import com.zavgorodniy.mytweets.network.modules.TweetsModuleImpl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

    private const val API_ENDPOINT = BuildConfig.ENDPOINT
    const val API_VERSION = "1.1/"
    const val AUTH_PARAM = "Authorization"
    const val CONTENT_TYPE_PARAM = "Content-Type"
    const val CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded;charset=UTF-8"
    const val GRANT_TYPE = "grant_type"
    const val GRANT_TYPE_VALUE = "client_credentials"
    private const val TIMEOUT_IN_SECONDS = 30L

    val mapper: ObjectMapper = ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(JodaModule())

    private val retrofit = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create(mapper))
        .baseUrl(API_ENDPOINT)
        .client(createHttpClient())
        .build()

    private fun createHttpClient() =
        OkHttpClient.Builder().apply {
            connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                    requestBuilder.method(original.method(), original.body())
                    return@addInterceptor chain.proceed(addAuth(original))
                }
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(loggingInterceptor)
        }.build()

    private fun addAuth(request: Request): Request {
        val urlBuilder = request.url().newBuilder()
        with(App.instance.getCurrentSession()) {
            if (!token.isNullOrEmpty()) {
                urlBuilder.addQueryParameter(AUTH_PARAM, token)
                urlBuilder.addQueryParameter(CONTENT_TYPE_PARAM, CONTENT_TYPE_VALUE)
            }
        }
        return request.newBuilder().url(urlBuilder.build()).build()
    }

    fun getTweetsModule() = TweetsModuleImpl(retrofit.create(TweetsApi::class.java))
}