package com.devsumos.coinpeek.data.repo

import com.devsumos.coinpeek.data.remote.CoinDeskApi
import com.devsumos.coinpeek.data.remote.CoinPriceResponse
import javax.inject.Inject

class CoinRepository @Inject constructor(
    private val api: CoinDeskApi
) {
    suspend fun getSingleCoinPrice(instrument: String, currency: CURRENCY): CoinPriceResponse {
        val list = mutableListOf<String>(instrument.buildInstrumentName(currency))
        return api.getCoinPrice(list)
    }

    suspend fun getAllCoinPrices(currency: CURRENCY): CoinPriceResponse {
        val list = mutableListOf<String>()
        INSTRUMENTS.entries.forEach {
            list.add(it.name.buildInstrumentName(currency))
        }
        return api.getCoinPrice(list)
    }
}

private fun String.buildInstrumentName(currency: CURRENCY) = "$this-$currency"

enum class INSTRUMENTS {
    BTC,
    ETH,
    DOT,
    XRP,
    USDT,
    BNB,
    DOGE,
}

enum class CURRENCY(val sign: String) {
    USD("$"),
    EUR("€"),
}