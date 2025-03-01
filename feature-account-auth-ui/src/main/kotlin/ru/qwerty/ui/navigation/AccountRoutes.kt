package ru.qwerty.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.qwerty.ui.presentation.compose.AccountScreen

/**
 * @author y.yangel
 */
sealed class AccountRoutes(val route: String) {
    data object Account : AccountRoutes("login")
}

fun NavGraphBuilder.buildAccountGraph() {
    composable(route = AccountRoutes.Account.route) {
        AccountScreen(viewModel = hiltViewModel())
    }
}