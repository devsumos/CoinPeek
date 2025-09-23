package com.devsumos.coinpeek.data.repo

import com.devsumos.coinpeek.data.remote.CoinDeskApi
import com.devsumos.coinpeek.data.remote.CoinPriceResponse
import javax.inject.Inject

class CoinRepository @Inject constructor(
    private val api: CoinDeskApi
) {
    suspend fun getCoinPrice(): CoinPriceResponse = api.getCoinPrice()
}