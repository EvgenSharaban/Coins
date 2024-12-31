package com.example.customviewwithoutcompose.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitClient @Inject constructor(
    httpLoggingInterceptor: HttpLoggingInterceptor,
) {
    private val client = OkHttpClient().newBuilder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun create(): ApiService = retrofit.create()

    companion object {
        private const val BASE_URL = "https://api.coinpaprika.com/v1/"
    }

}