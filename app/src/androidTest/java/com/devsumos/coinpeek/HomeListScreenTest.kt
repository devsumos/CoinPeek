package com.devsumos.coinpeek

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.devsumos.coinpeek.data.repo.CURRENCY
import com.devsumos.coinpeek.domain.CoinDetails
import com.devsumos.coinpeek.domain.HomeViewModel
import com.devsumos.coinpeek.ui.screen.HomeListScreen
import com.devsumos.coinpeek.ui.screen.formatAsPrice
import org.junit.Test
import org.junit.Rule

class HomeListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun everything_display_correctly() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val lastUpdatedTimestamp = "2025.09.26 23:00"
        val currency = CURRENCY.USD
        val btcValue = 12345.0
        val ethValue = 98765.0
        val btcChange = 0.37
        val ethChange = 0.45
        val state = HomeViewModel.State(
            selectedCurrency = currency,
            lastUpdatedTimestamp = lastUpdatedTimestamp,
            allCoinDetails = listOf(
                CoinDetails(
                    title = "BTC-USD",
                    value = btcValue,
                    valueFlag = "UP",
                    lastUpdatedTimestamp = lastUpdatedTimestamp,
                    dayHigh = 12400.0,
                    dayLow = 12200.0,
                    dayOpen = 12300.0,
                    dailyChange = 45.0,
                    dailyChangePercentage = btcChange
                ),
                CoinDetails(
                    title = "ETH-USD",
                    value = ethValue,
                    valueFlag = "DOWN",
                    lastUpdatedTimestamp = lastUpdatedTimestamp,
                    dayHigh = 12400.0,
                    dayLow = 12200.0,
                    dayOpen = 12300.0,
                    dailyChange = 45.0,
                    dailyChangePercentage = ethChange
                )
            )
        )

        composeTestRule.apply {
            setContent {
                HomeListScreen(
                    state = state,
                    onCoinClick = { },
                    onCurrencyClick = { },
                    onRefresh = { }
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
            onNodeWithText("${currency.sign}${btcValue.formatAsPrice()}").assertIsDisplayed()
            onNodeWithText("%${btcChange.formatAsPrice()}").assertIsDisplayed()
            onNodeWithTag("up_arrow", useUnmergedTree = true).assertIsDisplayed()

            onNodeWithText("ETH-USD").assertIsDisplayed()
            onNodeWithText("${currency.sign}${ethValue.formatAsPrice()}").assertIsDisplayed()
            onNodeWithText("%${ethChange.formatAsPrice()}").assertIsDisplayed()
            onNodeWithTag("down_arrow", useUnmergedTree = true).assertIsDisplayed()

        }
    }
}