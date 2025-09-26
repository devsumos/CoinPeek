package com.devsumos.coinpeek

import com.devsumos.coinpeek.data.remote.CoinData
import com.devsumos.coinpeek.data.remote.CoinPriceResponse
import com.devsumos.coinpeek.data.remote.toCoinDetails
import com.devsumos.coinpeek.data.repo.CURRENCY
import com.devsumos.coinpeek.data.repo.CoinRepository
import com.devsumos.coinpeek.domain.GetCoinDescriptionUrlUseCase
import com.devsumos.coinpeek.domain.SingleCoinViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import org.mockito.kotlin.wheneverBlocking

class SingleCoinViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SingleCoinViewModel

    private val coinRepository: CoinRepository = mock()
    private val getCoinDescriptionUrlUseCase: GetCoinDescriptionUrlUseCase = mock()

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
            )
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
            )
        ),
        err = null,
    )

    @Before
    fun setUp() {
        wheneverBlocking {
            coinRepository.getSingleCoinPrice(
                "BTC",
                CURRENCY.USD
            )
        } doReturn responseUSD
        wheneverBlocking {
            coinRepository.getSingleCoinPrice(
                "BTC",
                CURRENCY.EUR
            )
        } doReturn responseEUR
        whenever(getCoinDescriptionUrlUseCase.invoke(any())).thenReturn(
            Pair(
                R.string.btc_description,
                R.string.btc_url,
            )
        )
        viewModel = SingleCoinViewModel(
            coinName = "BTC",
            coinRepository,
            getCoinDescriptionUrlUseCase
        )
    }

    @Test
    fun `on init state has updated correctly`() = runTest {
        val expectedCoinDetails = responseUSD.data.values.first().toCoinDetails()

        assertEquals(expectedCoinDetails, viewModel.state.value.coinDetails)
        assertEquals(CURRENCY.USD, viewModel.state.value.selectedCurrency)

        assertEquals(false, viewModel.state.value.isLoading)
        assertEquals(false, viewModel.state.value.showError)

        assertEquals(R.string.btc_description, viewModel.state.value.descriptionResId)
        assertEquals(R.string.btc_url, viewModel.state.value.urlResId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `change currency update values correctly`() = runTest {
        viewModel.onCurrencyClick(CURRENCY.EUR)

        advanceUntilIdle()

        val expectedCoinDetails = responseEUR.data.values.first().toCoinDetails()

        assertEquals(expectedCoinDetails, viewModel.state.value.coinDetails)
        assertEquals(CURRENCY.EUR, viewModel.state.value.selectedCurrency)

        assertEquals(false, viewModel.state.value.isLoading)
        assertEquals(false, viewModel.state.value.showError)

        assertEquals(R.string.btc_description, viewModel.state.value.descriptionResId)
        assertEquals(R.string.btc_url, viewModel.state.value.urlResId)
    }

    @Test
    fun `on url click navigate to url`() = runTest {
        val url = "ww.com"
        viewModel.onLinkClick(url)

        assertEquals(url, viewModel.navigationState.value)
    }

    @Test
    fun `on reset navigation`() = runTest {
        viewModel.resetNavigation()

        assertEquals("", viewModel.navigationState.value)
    }


}