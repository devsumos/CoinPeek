package com.devsumos.coinpeek.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devsumos.coinpeek.data.remote.toCoinDetailsList
import com.devsumos.coinpeek.data.repo.CURRENCY
import com.devsumos.coinpeek.data.repo.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CoinRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        State()
    )
    val state = _state.asStateFlow()

    init {
        fetchData(CURRENCY.USD)
    }

    fun fetchData(currency: CURRENCY) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                )
            }
            try {
                val allCoinPrices = repository.getAllCoinPrices(currency = currency)
                val list = allCoinPrices.toCoinDetailsList()
                _state.update {
                    it.copy(
                        allCoinDetails = list,
                        showError = false,
                        selectedCurrency = currency,
                        lastUpdatedTimestamp = list.first().lastUpdatedTimestamp,
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

    fun onCurrencyClick(currency: CURRENCY) {
        fetchData(currency)
    }

    data class State(
        val allCoinDetails: List<CoinDetails>? = null,
        val lastUpdatedTimestamp: String = "",
        val selectedCurrency: CURRENCY? = null,
        val isLoading: Boolean = false,
        val showError: Boolean = false,
    )
}