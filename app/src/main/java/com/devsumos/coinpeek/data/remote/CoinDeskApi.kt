package com.devsumos.coinpeek.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CoinDeskApi {

    @GET("/index/cc/v1/latest/tick?market=cadli")
    suspend fun getCoinPrice(
        @Query("instruments") instruments: List<String>,
    ): CoinPriceResponse
}