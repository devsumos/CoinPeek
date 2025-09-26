package com.devsumos.coinpeek.domain

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devsumos.coinpeek.data.remote.toCoinDetails
import com.devsumos.coinpeek.data.repo.CURRENCY
import com.devsumos.coinpeek.data.repo.CoinRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SingleCoinViewModel.Factory::class)
class SingleCoinViewModel @AssistedInject constructor(
    @Assisted private val coinName: String = "",
    private val coinRepository: CoinRepository,
    private val getCoinDescriptionUrlUseCase: GetCoinDescriptionUrlUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val _navigationState = MutableStateFlow<String>("")
    val navigationState = _navigationState.asStateFlow()

    init {
        fetchData(CURRENCY.USD)
        updateDescription()
    }

    private fun updateDescription() {
        val resIdPair = getCoinDescriptionUrlUseCase(coinName)
        _state.update {
            it.copy(
                descriptionResId = resIdPair.first,
                urlResId = resIdPair.second,
            )
        }
    }

    fun fetchData(currency: CURRENCY) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                )
            }
            try {
                val allCoinPrices = coinRepository.getSingleCoinPrice(coinName, currency)
                val coinDetails = allCoinPrices.data.values.first().toCoinDetails()
                _state.update {
                    it.copy(
                        coinDetails = coinDetails,
                        showError = false,
                        selectedCurrency = currency,
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

    fun onLinkClick(url: String) {
        _navigationState.update { url }
    }

    fun resetNavigation() {
        _navigationState.update { "" }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            coinName: String,
        ): SingleCoinViewModel
    }

    data class State(
        val coinDetails: CoinDetails? = null,
        val selectedCurrency: CURRENCY? = null,
        @StringRes val descriptionResId: Int = -1,
        @StringRes val urlResId: Int = -1,
        val isLoading: Boolean = false,
        val showError: Boolean = false,
    )
}