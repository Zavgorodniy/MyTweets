package com.zavgorodniy.mytweets.network

import android.util.Base64
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
import com.zavgorodniy.mytweets.network.api.TweetsApi
import com.zavgorodniy.mytweets.utils.TokenType
import okhttp3.MediaType
import okhttp3.RequestBody
import java.net.URLEncoder

object NetworkModule {

    private const val API_ENDPOINT = BuildConfig.ENDPOINT
    private const val AUTH_PARAM = "Authorization"
    private const val CONTENT_TYPE_PARAM = "Content-Type"
    private const val CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded;charset=UTF-8"
    private const val TIMEOUT_IN_SECONDS = 30L

    const val API_VERSION = "1.1/"
    const val GRANT_TYPE_VALUE = "grant_type=client_credentials"

    private val mapper: ObjectMapper = ObjectMapper()
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
                    return@addInterceptor chain.proceed(addAuth(requestBuilder))
                }
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(loggingInterceptor)
        }.build()

    private fun addAuth(requestBuilder: Request.Builder): Request {
        requestBuilder.removeHeader(AUTH_PARAM)
        requestBuilder.removeHeader(CONTENT_TYPE_PARAM)
        with(App.instance.getCurrentSession()) {
            if (type == TokenType.TYPE_BEARER() && !token.isNullOrEmpty()) {
                requestBuilder.addHeader(AUTH_PARAM, token)
            } else {
                requestBuilder.addHeader(AUTH_PARAM, makeAuthHeader())
                requestBuilder.addHeader(CONTENT_TYPE_PARAM, CONTENT_TYPE_VALUE)
                requestBuilder.post(RequestBody.create(MediaType.parse(CONTENT_TYPE_VALUE), GRANT_TYPE_VALUE))
            }
        }
        return requestBuilder.build()
    }

    fun getTweetsModule() = TweetsModuleImpl(retrofit.create(TweetsApi::class.java))

    private fun makeAuthHeader() : String {
        val encodedConsumerKey = URLEncoder.encode(BuildConfig.TWITTER_CONSUMER_KEY, "UTF-8")
        val encodedConsumerSecret = URLEncoder.encode(BuildConfig.TWITTER_CONSUMER_SECRET, "UTF-8")
        val fullKey = "$encodedConsumerKey:$encodedConsumerSecret"
        val base64Encoded = Base64.encodeToString(fullKey.toByteArray(), Base64.NO_WRAP)
        return "Basic $base64Encoded"
    }
}