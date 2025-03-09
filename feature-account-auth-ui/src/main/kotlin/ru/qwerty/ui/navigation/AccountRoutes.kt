package ru.qwerty.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.qwerty.ui.presentation.compose.UserLoginScreen
import ru.qwerty.ui.presentation.compose.splash.SplashScreen

/**
 * @author y.yangel
 */
sealed class AccountRoutes(val route: String) {
    data object Account : AccountRoutes(ACCOUNT_LOGIN_NAVIGATION_KEY)
    data object AccountListScreen : AccountRoutes(ACCOUNT_LIST_NAVIGATION_KEY)
    data object SplashScreen : AccountRoutes(ACCOUNT_SPLASH_NAVIGATION_KEY)

    private companion object {
        private const val ACCOUNT_LIST_NAVIGATION_KEY = "account-list"
        private const val ACCOUNT_LOGIN_NAVIGATION_KEY = "login"
        private const val ACCOUNT_SPLASH_NAVIGATION_KEY = "splash"
    }
}

fun NavController.navigateToAccountList() {
    navigate(AccountRoutes.AccountListScreen.route) {
        popUpTo(AccountRoutes.Account.route) {
            inclusive = true
        }
    }
}

fun NavController.navigateToLogin() {
    navigate(AccountRoutes.Account.route) {
        popUpTo(AccountRoutes.SplashScreen.route) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.buildAccountGraph(
    onNavigateToAccountList: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    composable(route = AccountRoutes.Account.route) {
        UserLoginScreen(
            viewModel = hiltViewModel(),
            navigateOnAccountList = onNavigateToAccountList,
        )
    }
    composable(route = AccountRoutes.SplashScreen.route) {
        SplashScreen(
            viewModel = hiltViewModel(),
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToAccountList = onNavigateToAccountList,
        )
    }
}