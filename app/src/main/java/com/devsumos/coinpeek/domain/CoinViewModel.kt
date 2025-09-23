package com.devsumos.coinpeek.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devsumos.coinpeek.data.remote.toCoinDetails
import com.devsumos.coinpeek.data.repo.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val BTC_USD_DATA_NAME = "BTC-USD"
private const val ETH_USD_DATA_NAME = "ETH-USD"
private const val DOT_USD_DATA_NAME = "DOT-USD"

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val repository: CoinRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        State()
    )
    val state = _state.asStateFlow()

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            try {
                val response = repository.getCoinPrice()
                val bitcoinDetails = response.data[BTC_USD_DATA_NAME]?.toCoinDetails()
                val etheriumDetails = response.data[ETH_USD_DATA_NAME]?.toCoinDetails()
                val polkaDetails = response.data[DOT_USD_DATA_NAME]?.toCoinDetails()
                _state.update {
                    it.copy(
                        bitcoinDetails = bitcoinDetails,
                        etheriumDetails = etheriumDetails,
                        polkaDetails = polkaDetails,
                        showError = false,
                    )
                }

            } catch (_: Exception) {
                _state.update {
                    it.copy(
                        showError = true,
                    )
                }

            } finally {
                _state.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    data class State(
        val bitcoinDetails: CoinDetails? = null,
        val etheriumDetails: CoinDetails? = null,
        val polkaDetails: CoinDetails? = null,
        val isLoading: Boolean = false,
        val showError: Boolean = false,

        )
}