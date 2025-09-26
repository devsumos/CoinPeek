package com.devsumos.coinpeek.ui.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devsumos.coinpeek.R
import com.devsumos.coinpeek.data.repo.CURRENCY
import com.devsumos.coinpeek.domain.CoinDetails
import com.devsumos.coinpeek.domain.SingleCoinViewModel
import com.devsumos.coinpeek.ui.theme.CoinPeekTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleCoinScreen(
    state: SingleCoinViewModel.State,
    onCurrencyClick: (CURRENCY) -> Unit,
    onRefresh: (CURRENCY) -> Unit,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLoading = state.isLoading
    val coinDetails = state.coinDetails

    Scaffold(
        containerColor = Color.Black,
        contentWindowInsets = WindowInsets.safeDrawing,
        content = { padding ->
            ScreenContent(
                modifier = modifier,
                padding = padding,
                coinDetails = coinDetails,
                selectedCurrency = state.selectedCurrency,
                descriptionResId = state.descriptionResId,
                urlResId = state.urlResId,
                isLoading = isLoading,
                onCurrencyClick = onCurrencyClick,
                onRefresh = onRefresh,
                onLinkClick = onLinkClick,
            )
        }
    )

}

@Composable
private fun ScreenContent(
    modifier: Modifier,
    padding: PaddingValues,
    coinDetails: CoinDetails? = null,
    selectedCurrency: CURRENCY? = null,
    descriptionResId: Int = -1,
    urlResId: Int = -1,
    isLoading: Boolean = false,
    onCurrencyClick: (CURRENCY) -> Unit,
    onRefresh: (CURRENCY) -> Unit,
    onLinkClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        selectedCurrency?.let {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CurrencySelector(
                        selectedCurrency = selectedCurrency,
                        onSelected = onCurrencyClick,
                        modifier = Modifier.weight(1f),
                    )
                    RefreshItem(
                        coinDetails?.lastUpdatedTimestamp ?: "",
                        selectedCurrency,
                        onRefresh,
                    )
                }
            }
        }
        if (isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else if (coinDetails == null) {
            item {
                Text(stringResource(R.string.no_data_available))
            }
        } else {
            item {
                CoinDataCard(
                    coinDetails = coinDetails,
                )
            }
        }
        item {
            CoinInformationCard(
                descriptionResId,
                urlResId,
                onLinkClick,
            )
        }
    }
}

@Composable
fun CoinInformationCard(
    @StringRes descriptionResId: Int,
    @StringRes urlResId: Int,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val url = stringResource(urlResId)
    Card(
        modifier = modifier
            .fillMaxWidth(),
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
            Text(
                text = stringResource(descriptionResId),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.White,
            )
            Row(
                modifier = Modifier.clickable {
                    onLinkClick(url)
                },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = url,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF90CAF9),
                    modifier = Modifier.padding(end = 4.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.rounded_open_in_new_24),
                    tint = Color(0xFF90CAF9),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun CoinDataCard(
    coinDetails: CoinDetails,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (coinDetails.valueFlag == "UP") {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Up",
                        tint = Color.Green,
                        modifier = Modifier.testTag("up_arrow"),
                    )
                    Text(
                        text = "%${coinDetails.dailyChangePercentage.formatAsPrice()}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(end = 8.dp),
                        color = Color.Green,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Down",
                        tint = Color.Red,
                        modifier = Modifier.testTag("down_arrow"),
                    )
                    Text(
                        text = "%${coinDetails.dailyChangePercentage.formatAsPrice()}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(end = 8.dp),
                        color = Color.Red,
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatRow(stringResource(R.string.high_title), coinDetails.dayHigh)
                    StatRow(stringResource(R.string.low_title), coinDetails.dayLow)
                    StatRow(stringResource(R.string.open_title), coinDetails.dayOpen)
                }
            }
        }
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
        SingleCoinScreen(
            SingleCoinViewModel.State(
                coinDetails = CoinDetails(
                    title = "BTC-USD",
                    value = 124335.35,
                    valueFlag = "up",
                    lastUpdatedTimestamp = "2025.44.12",
                    dayHigh = 1243154653.253245,
                    dayLow = 124.123123,
                    dayOpen = 1413.1231,
                    dailyChange = -23.75156886816,
                    dailyChangePercentage = -0.567315041023957,
                ),
                selectedCurrency = CURRENCY.USD,
                urlResId = R.string.btc_url,
                descriptionResId = R.string.btc_description
            ),
            {},
            {},
            {},
        )
    }
}