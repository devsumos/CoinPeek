package com.devsumos.coinpeek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devsumos.coinpeek.domain.CoinViewModel
import com.devsumos.coinpeek.ui.screen.CoinListScreen
import com.devsumos.coinpeek.ui.theme.CoinPeekTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoinPeekTheme {
                val viewModel = hiltViewModel<CoinViewModel>()
                val state = viewModel.state.collectAsStateWithLifecycle().value
                CoinListScreen(
                    state = state,
                    onRefresh = viewModel::fetchData,
                )
            }
        }
    }
}