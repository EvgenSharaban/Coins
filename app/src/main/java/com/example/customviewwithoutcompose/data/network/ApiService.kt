package com.example.customviewwithoutcompose.data.network

import com.example.customviewwithoutcompose.data.network.entities.CoinEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("coins")
    suspend fun getCoins(): Response<List<CoinEntity>>

    @GET("coins/{coin_id}")
    suspend fun getCoinById(
        @Path("coin_id") coinId: String
    ): Response<CoinEntity>

    @GET("tickers/{coin_id}")
    suspend fun getTickerById(
        @Path("coin_id") coinId: String
    ): Response<CoinEntity>

}