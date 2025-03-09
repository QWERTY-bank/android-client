package ru.qwerty.feature.credits.ui.presentation.navigation

import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.qwerty.feature.credits.ui.presentation.composable.tariff.TariffListScreen

/**
 * @author y.yangel
 */
sealed class CreditsNavigationRoute(val route: String) {
    data object TariffListScreenRoute : CreditsNavigationRoute(TariffListScreenRouteKey)

    private companion object {
        private const val TariffListScreenRouteKey = "TariffListScreenRouteKey"
    }
}

fun NavController.navigateToTariffListScreen() {
    navigate(CreditsNavigationRoute.TariffListScreenRoute.route)
}

fun NavGraphBuilder.buildCreditsFeatureGraph() {
    composable(CreditsNavigationRoute.TariffListScreenRoute.route) {
        TariffListScreen(viewModel = hiltViewModel())
    }
}