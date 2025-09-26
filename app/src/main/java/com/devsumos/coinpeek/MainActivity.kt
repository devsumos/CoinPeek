package com.devsumos.coinpeek

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devsumos.coinpeek.domain.HomeViewModel
import com.devsumos.coinpeek.domain.SingleCoinViewModel
import com.devsumos.coinpeek.ui.screen.SingleCoinScreen
import com.devsumos.coinpeek.ui.screen.HomeListScreen
import com.devsumos.coinpeek.ui.theme.CoinPeekTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoinPeekTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.HomeScreen.route,
                ) {
                    composable(route = Screen.HomeScreen.route) {
                        val viewModel = hiltViewModel<HomeViewModel>()
                        val state = viewModel.state.collectAsStateWithLifecycle().value
                        HomeListScreen(
                            state = state,
                            onCurrencyClick = viewModel::onCurrencyClick,
                            onRefresh = viewModel::fetchData,
                            onCoinClick = { name ->
                                navController.navigate(
                                    route = Screen.SingleCoinScreen.route + "?coinName=${name}"
                                )
                            },
                        )
                    }
                    composable(
                        route = Screen.SingleCoinScreen.route + "?coinName={coinName}",
                        arguments = listOf(
                            navArgument("coinName") {
                                type = NavType.StringType
                                nullable = false
                            },
                        )
                    ) {
                        val viewModel =
                            hiltViewModel<SingleCoinViewModel, SingleCoinViewModel.Factory> { factory ->
                                factory.create(
                                    coinName = it.arguments?.getString("coinName") ?: ""
                                )
                            }
                        val state = viewModel.state.collectAsStateWithLifecycle().value
                        val navigationState = viewModel.navigationState.collectAsState()
                        SingleCoinScreen(
                            state = state,
                            onCurrencyClick = viewModel::onCurrencyClick,
                            onRefresh = viewModel::fetchData,
                            onLinkClick = viewModel::onLinkClick,
                        )
                        LaunchedEffect(navigationState.value) {
                            if (navigationState.value.isNotEmpty()) {
                                val intent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(navigationState.value))
                                startActivity(intent)
                                viewModel.resetNavigation()
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object SingleCoinScreen : Screen("single_coin_screen")
}