package com.devsumos.coinpeek.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.devsumos.coinpeek.domain.CoinViewModel
import com.devsumos.coinpeek.domain.CoinDetails
import com.devsumos.coinpeek.ui.theme.CoinPeekTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinListScreen(
    state: CoinViewModel.State,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
) {
    val isLoading = state.isLoading
    val bitcoinDetails = state.bitcoinDetails
    val etheriumDetails = state.etheriumDetails
    val polkaDetails = state.polkaDetails

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopBar(onRefresh = onRefresh)
        },
        content = { padding ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (isLoading) {
                    item {
                        CircularProgressIndicator()
                    }
                } else if (bitcoinDetails == null &&
                    etheriumDetails == null &&
                    polkaDetails == null
                ) {
                    item {
                        Text("No data available.")
                    }
                } else {
                    if (bitcoinDetails != null) {
                        item {
                            CoinDataCard(
                                coinDetails = bitcoinDetails,
                            )
                        }
                    }

                    if (etheriumDetails != null) {
                        item {
                            CoinDataCard(
                                coinDetails = etheriumDetails,
                            )
                        }
                    }

                    if (polkaDetails != null) {
                        item {
                            CoinDataCard(
                                coinDetails = polkaDetails,
                            )
                        }
                    }
                }
            }
        }
    )

}

@Composable
fun CoinDataCard(
    coinDetails: CoinDetails,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = Color.DarkGray
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            coinDetails.title?.let {
                CoinTitle(
                    title = coinDetails.title,
                    value = coinDetails.value,
                )
            }

            if (coinDetails.valueFlag == "UP") {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Up",
                    tint = Color.Green
                )
            } else if (coinDetails.valueFlag == "DOWN") {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Down",
                    tint = Color.Red
                )
            }

            Text(
                text = "Last updated: ${coinDetails.lastUpdatedTimestamp}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatRow("High", coinDetails.dayHigh)
                    StatRow("Low", coinDetails.dayLow)
                    StatRow("Open", coinDetails.dayOpen)
                }
            }
        }
    }
}


@Composable
fun TopBar(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
        )
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Refresh",
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable(onClick = onRefresh),
        )
    }
}

@Composable
fun CoinTitle(
    title: String,
    value: Double?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = Color.White,
        )
        Text(
            text = value.formatAsPrice(),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = Color.White,
        )
    }
}

@Composable
fun StatRow(label: String, value: Double?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text("$value")
    }
}

fun Double?.formatAsPrice(): String = String.format("%,.2f", this)

@Preview
@Composable
fun Preview() {
    CoinPeekTheme {
        CoinListScreen(
            CoinViewModel.State(
                bitcoinDetails = CoinDetails(
                    title = "BTC-USD",
                    value = 124335.35,
                    valueFlag = "up",
                    lastUpdatedTimestamp = "2025.44.12",
                    dayHigh = 1243154653.253245,
                    dayLow = 124.123123,
                    dayOpen = 1413.1231,
                ),
                etheriumDetails = CoinDetails(
                    title = "ETH-USD",
                    value = 124335.35,
                    valueFlag = "down",
                    lastUpdatedTimestamp = "2025.44.12",
                    dayHigh = 1243154653.253245,
                    dayLow = 124.123123,
                    dayOpen = 1413.1231,
                ),
            )
        )
    }
}