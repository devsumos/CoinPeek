package com.devsumos.coinpeek.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devsumos.coinpeek.R
import com.devsumos.coinpeek.data.repo.CURRENCY
import com.devsumos.coinpeek.domain.CoinDetails
import com.devsumos.coinpeek.domain.HomeViewModel
import com.devsumos.coinpeek.ui.theme.CoinPeekTheme

@Composable
fun HomeListScreen(
    state: HomeViewModel.State,
    onCoinClick: (String) -> Unit,
    onCurrencyClick: (CURRENCY) -> Unit,
    onRefresh: (CURRENCY) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        containerColor = Color.Black,
        contentWindowInsets = WindowInsets.safeDrawing,
        content = { padding ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                state.selectedCurrency?.let {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CurrencySelector(
                                selectedCurrency = state.selectedCurrency,
                                onSelected = onCurrencyClick,
                                modifier = Modifier.weight(1f),
                            )
                            RefreshItem(
                                state.lastUpdatedTimestamp,
                                state.selectedCurrency,
                                onRefresh,
                            )
                        }
                    }
                }
                state.allCoinDetails?.forEach { coin ->
                    item {
                        CoinRow(
                            coin,
                            state.selectedCurrency?.sign ?: "",
                            onCoinClick,
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun RefreshItem(
    lastUpdatedTimestamp: String,
    selectedCurrency: CURRENCY,
    onRefresh: (CURRENCY) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Refresh",
            tint = Color.White,
            modifier = Modifier
                .padding(bottom = 4.dp)
                .clickable {
                    onRefresh(selectedCurrency)
                },
        )
        Text(
            text = stringResource(R.string.last_updated, lastUpdatedTimestamp),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = Color.White,
        )
    }
}

@Composable
fun CurrencySelector(
    selectedCurrency: CURRENCY,
    onSelected: (CURRENCY) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CURRENCY.entries.forEach { currency ->
            val isSelected = selectedCurrency != currency
            Card(
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors().copy(
                    containerColor = if (isSelected) {
                        Color.DarkGray
                    } else {
                        Color.DarkGray.copy(alpha = 0.5f)
                    }
                ),
                modifier = Modifier.clickable {
                    if (isSelected) {
                        onSelected(currency)
                    }
                }
            ) {
                Text(
                    text = currency.name,
                    color = Color.White,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun CoinRow(
    coin: CoinDetails,
    currencySign: String,
    onCoinClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onCoinClick(coin.title?.baseSymbol() ?: "")
            },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors().copy(
            containerColor = Color.DarkGray
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = coin.title ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "${currencySign}${coin.value.formatAsPrice()}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f),
            )
            if (coin.valueFlag == "UP") {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Up",
                    tint = Color.Green,
                )
                Text(
                    text = "%${coin.dailyChangePercentage.formatAsPrice()}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(end = 8.dp),
                    color = Color.Green,
                )
            } else {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Down",
                    tint = Color.Red,
                )
                Text(
                    text = "%${coin.dailyChangePercentage.formatAsPrice()}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(end = 8.dp),
                    color = Color.Red,
                )
            }
        }
    }
}

private fun String.baseSymbol(): String = this.substringBefore("-")

@Preview
@Composable
private fun HomeListScreenPreview() {
    CoinPeekTheme {
        HomeListScreen(
            onCoinClick = {},
            onCurrencyClick = {},
            onRefresh = {},
            state = HomeViewModel.State(
                selectedCurrency = CURRENCY.EUR,
                lastUpdatedTimestamp = "2024.10.23 23.14",
                allCoinDetails = listOf(
                    CoinDetails(
                        title = "BTC",
                        value = 124335.35,
                        valueFlag = "UP",
                        lastUpdatedTimestamp = "2025.44.12",
                        dayHigh = 1243154653.253245,
                        dayLow = 124.123123,
                        dayOpen = 1413.1231,
                        dailyChange = -23.75156886816,
                        dailyChangePercentage = -0.567315041023957,
                    ),
                    CoinDetails(
                        title = "ETH",
                        value = 124335.35,
                        valueFlag = "down",
                        lastUpdatedTimestamp = "2025.44.12",
                        dayHigh = 1243154653.253245,
                        dayLow = 124.123123,
                        dayOpen = 1413.1231,
                        dailyChange = -23.75156886816,
                        dailyChangePercentage = -0.567315041023957,
                    ),
                )
            )
        )
    }
}