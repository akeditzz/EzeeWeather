package com.amshotzz.ezeeweather.api.network

import com.amshotzz.ezeeweather.api.token.TokenAuthenticator
import com.amshotzz.ezeeweather.application.EzeeWeatherApplication
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object Networking {

    private const val NETWORK_CALL_TIMEOUT = 60
    private lateinit var API_KEY: String

    fun create(
        apiKey: String,
        baseUrl: String,
        cacheDir: File,
        cacheSize: Long,
        application: EzeeWeatherApplication
    ): NetworkService {
        API_KEY = apiKey
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                OkHttpClient.Builder()
                    .cache(Cache(cacheDir, cacheSize))
                    .addInterceptor(TokenAuthenticator(application))
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .apply {
                                level =  HttpLoggingInterceptor.Level.BODY
                            })
                    .readTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .connectTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(JacksonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(NetworkService::class.java)
    }
}