package com.devsumos.coinpeek.domain

data class CoinDetails(
    val title: String?,
    val value: Double?,
    val valueFlag: String?,
    val lastUpdatedTimestamp: String = "",
    val dayHigh: Double?,
    val dayLow: Double?,
    val dayOpen: Double?
)