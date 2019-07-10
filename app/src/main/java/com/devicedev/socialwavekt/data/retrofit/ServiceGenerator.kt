package com.devicedev.socialwavekt.data.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceGenerator {

    const val BASE_URL = "http://192.168.1.6:5000/"

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttp = OkHttpClient.Builder()
    init {
        okHttp.connectTimeout(5, TimeUnit.SECONDS)
    }

    private val builder = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.addInterceptor(interceptor).build())

    private val retrofit = builder.build()

    fun <T> create(type: Class<T>): T = retrofit.create(type)

}