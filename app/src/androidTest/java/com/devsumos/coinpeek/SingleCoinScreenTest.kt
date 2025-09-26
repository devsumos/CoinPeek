package com.devsumos.coinpeek

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.devsumos.coinpeek.data.repo.CURRENCY
import com.devsumos.coinpeek.domain.CoinDetails
import com.devsumos.coinpeek.domain.SingleCoinViewModel
import com.devsumos.coinpeek.ui.screen.SingleCoinScreen
import com.devsumos.coinpeek.ui.screen.formatAsPrice
import org.junit.Test
import org.junit.Rule

class SingleCoinScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun everything_display_correctly() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val lastUpdatedTimestamp = "2025.09.26 23:00"
        val currency = CURRENCY.USD
        val btcValue = 12345.0
        val btcChange = 0.37
        val dayHigh = 12400.0
        val dayLow = 12200.0
        val dayOpen = 12300.0
        val state = SingleCoinViewModel.State(
            selectedCurrency = currency,
            coinDetails = CoinDetails(
                title = "BTC-USD",
                value = btcValue,
                valueFlag = "UP",
                lastUpdatedTimestamp = lastUpdatedTimestamp,
                dayHigh = dayHigh,
                dayLow = dayLow,
                dayOpen = dayOpen,
                dailyChange = 45.0,
                dailyChangePercentage = btcChange,
            ),
            descriptionResId = R.string.btc_description,
            urlResId = R.string.btc_url,
        )

        composeTestRule.apply {
            setContent {
                SingleCoinScreen(
                    state = state,
                    onCurrencyClick = { },
                    onRefresh = {},
                    onLinkClick = {},
                )
            }
            onNodeWithText("BTC-USD").assertIsDisplayed()
            onNodeWithTag("refresh_icon").assertIsDisplayed()
            onNodeWithText(
                context.getString(
                    R.string.last_updated,
                    lastUpdatedTimestamp
                )
            ).assertIsDisplayed()

            onNodeWithText("USD").assertIsDisplayed()
            onNodeWithText("EUR").assertIsDisplayed()

            onNodeWithText("BTC-USD").assertIsDisplayed()
            onNodeWithText(btcValue.formatAsPrice()).assertIsDisplayed()
            onNodeWithText("%${btcChange.formatAsPrice()}").assertIsDisplayed()
            onNodeWithTag("up_arrow", useUnmergedTree = true).assertIsDisplayed()

            onNodeWithText("High").assertIsDisplayed()
            onNodeWithText("$dayHigh").assertIsDisplayed()
            onNodeWithText("Low").assertIsDisplayed()
            onNodeWithText("$dayLow").assertIsDisplayed()
            onNodeWithText("Open").assertIsDisplayed()
            onNodeWithText("$dayOpen").assertIsDisplayed()

            onNodeWithText(context.getString(R.string.btc_description)).assertIsDisplayed()
            onNodeWithText(context.getString(R.string.btc_url)).assertIsDisplayed()
        }
    }
}