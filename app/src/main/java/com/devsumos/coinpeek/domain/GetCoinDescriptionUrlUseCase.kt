package com.devsumos.coinpeek.domain

import com.devsumos.coinpeek.R
import com.devsumos.coinpeek.data.repo.INSTRUMENTS
import javax.inject.Inject

class GetCoinDescriptionUrlUseCase @Inject constructor() {

    operator fun invoke(coinName: String): Pair<Int, Int> {
        return when (coinName) {
            INSTRUMENTS.BTC.name -> Pair(R.string.btc_description, R.string.btc_url)
            INSTRUMENTS.ETH.name -> Pair(R.string.eth_description, R.string.eth_url)
            INSTRUMENTS.DOT.name -> Pair(R.string.dot_description, R.string.dot_url)
            INSTRUMENTS.XRP.name -> Pair(R.string.xrp_description, R.string.xrp_url)
            INSTRUMENTS.USDT.name -> Pair(R.string.usdt_description, R.string.usdt_url)
            INSTRUMENTS.BNB.name -> Pair(R.string.bnb_description, R.string.bnb_url)
            INSTRUMENTS.DOGE.name -> Pair(R.string.doge_description, R.string.doge_url)
            else -> Pair(-1, -1)
        }
    }
}