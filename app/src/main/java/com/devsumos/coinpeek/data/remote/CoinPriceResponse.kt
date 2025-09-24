package com.devsumos.coinpeek.data.remote

import com.devsumos.coinpeek.domain.CoinDetails
import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class CoinPriceResponse(
    @SerializedName("Data")
    val data: Map<String, CoinData>,
    @SerializedName("Err")
    val err: Any?
)

data class CoinData(
    @SerializedName("INSTRUMENT")
    val instrument: String?,
    @SerializedName("MARKET")
    val market: String?,
    @SerializedName("VALUE")
    val value: Double?,
    @SerializedName("VALUE_FLAG")
    val valueFlag: String?,
    @SerializedName("VALUE_LAST_UPDATE_TS")
    val lastUpdatedTimestamp: Long?,
    @SerializedName("CURRENT_DAY_HIGH")
    val dayHigh: Double?,
    @SerializedName("CURRENT_DAY_LOW")
    val dayLow: Double?,
    @SerializedName("CURRENT_DAY_OPEN")
    val dayOpen: Double?,
    @SerializedName("MOVING_24_HOUR_CHANGE")
    val dailyChange: Double?,
    @SerializedName("MOVING_24_HOUR_CHANGE_PERCENTAGE")
    val dailyChangePercentage: Double?
)

fun CoinData.toCoinDetails(): CoinDetails =
    CoinDetails(
        title = instrument,
        value = value,
        valueFlag = valueFlag,
        lastUpdatedTimestamp = getFormatedDate(lastUpdatedTimestamp),
        dayHigh = dayHigh,
        dayLow = dayLow,
        dayOpen = dayOpen,
        dailyChange = dailyChange,
        dailyChangePercentage = dailyChangePercentage,
    )

fun CoinPriceResponse.toCoinDetailsList(): List<CoinDetails> {
    val list = mutableListOf<CoinDetails>()
    this.data.forEach { _, coin -> list.add(coin.toCoinDetails()) }
    return list
}

private fun getFormatedDate(timeStamp: Long?): String {
    if (timeStamp == null) return ""
    return timeStamp.toFormattedDate()
}

fun Long.toFormattedDate(): String {
    val date = Instant.ofEpochSecond(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
}
