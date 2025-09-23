package com.devsumos.coinpeek.data.remote

import retrofit2.http.GET

interface CoinDeskApi {

    @GET("/index/cc/v1/latest/tick?market=cadli&instruments=BTC-USD,ETH-USD,DOT-USD")
    suspend fun getCoinPrice(): CoinPriceResponse
}