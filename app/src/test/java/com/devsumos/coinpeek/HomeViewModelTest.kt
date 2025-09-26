package com.devsumos.coinpeek

import com.devsumos.coinpeek.data.remote.CoinData
import com.devsumos.coinpeek.data.remote.CoinPriceResponse
import com.devsumos.coinpeek.data.remote.toCoinDetailsList
import com.devsumos.coinpeek.data.repo.CURRENCY
import com.devsumos.coinpeek.data.repo.CoinRepository
import com.devsumos.coinpeek.domain.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.wheneverBlocking

class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HomeViewModel

    private val coinRepository: CoinRepository = mock()

    private val responseUSD = CoinPriceResponse(
        data = mapOf<String, CoinData>(
            Pair(
                "BTC",
                CoinData(
                    instrument = "BTC-USD",
                    market = null,
                    value = 2312213.0,
                    valueFlag = "UP",
                    lastUpdatedTimestamp = System.currentTimeMillis(),
                    dayHigh = 123.123,
                    dayLow = 12.12,
                    dayOpen = 23.23,
                    dailyChange = null,
                    dailyChangePercentage = 0.5,
                )
            ),
            Pair(
                "ETH",
                CoinData(
                    instrument = "ETH-USD",
                    market = null,
                    value = 2312213.0,
                    valueFlag = "UP",
                    lastUpdatedTimestamp = System.currentTimeMillis(),
                    dayHigh = 123.123,
                    dayLow = 12.12,
                    dayOpen = 23.23,
                    dailyChange = null,
                    dailyChangePercentage = 0.5,
                )
            ),
        ),
        err = null,
    )

    private val responseEUR = CoinPriceResponse(
        data = mapOf<String, CoinData>(
            Pair(
                "BTC",
                CoinData(
                    instrument = "BTC-EUR",
                    market = null,
                    value = 2312213.0,
                    valueFlag = "UP",
                    lastUpdatedTimestamp = System.currentTimeMillis(),
                    dayHigh = 123.123,
                    dayLow = 12.12,
                    dayOpen = 23.23,
                    dailyChange = null,
                    dailyChangePercentage = 0.5,
                )
            ),
            Pair(
                "ETH",
                CoinData(
                    instrument = "ETH-EUR",
                    market = null,
                    value = 2312213.0,
                    valueFlag = "UP",
                    lastUpdatedTimestamp = System.currentTimeMillis(),
                    dayHigh = 123.123,
                    dayLow = 12.12,
                    dayOpen = 23.23,
                    dailyChange = null,
                    dailyChangePercentage = 0.5,
                )
            )
        ),
        err = null,
    )

    @Before
    fun setUp() {
        wheneverBlocking {
            coinRepository.getAllCoinPrices(
                CURRENCY.USD
            )
        } doReturn responseUSD
        wheneverBlocking {
            coinRepository.getAllCoinPrices(
                CURRENCY.EUR
            )
        } doReturn responseEUR
        viewModel = HomeViewModel(
            coinRepository,
        )
    }

    @Test
    fun `on init state has updated correctly`() = runTest {
        val expectedCoinDetails = responseUSD.toCoinDetailsList()

        assertEquals(expectedCoinDetails, viewModel.state.value.allCoinDetails)
        assertEquals(CURRENCY.USD, viewModel.state.value.selectedCurrency)

        assertEquals(false, viewModel.state.value.isLoading)
        assertEquals(false, viewModel.state.value.showError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `change currency update values correctly`() = runTest {
        viewModel.onCurrencyClick(CURRENCY.EUR)

        advanceUntilIdle()

        val expectedCoinDetails = responseEUR.toCoinDetailsList()

        assertEquals(expectedCoinDetails, viewModel.state.value.allCoinDetails)
        assertEquals(CURRENCY.EUR, viewModel.state.value.selectedCurrency)

        assertEquals(false, viewModel.state.value.isLoading)
        assertEquals(false, viewModel.state.value.showError)
    }

}