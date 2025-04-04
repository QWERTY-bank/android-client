package com.example.feature.account.ui.presentation.ui.navigation

import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.feature.account.ui.presentation.ui.accountdetails.AccountDetailsScreen
import com.example.feature.account.ui.presentation.ui.accountlist.AccountListScreen
import com.example.feature.account.ui.presentation.ui.deposit.DepositScreen
import com.example.feature.account.ui.presentation.ui.withdraw.WithdrawScreen

/**
 * @author y.yangel
 */
sealed class AccountListRoutes(val route: String) {
    data object AccountListScreen : AccountListRoutes(ACCOUNT_LIST_NAVIGATION_KEY)
    data object AccountDetailsScreen : AccountListRoutes(ACCOUNT_DETAILS_NAVIGATION_KEY)
    data object WithdrawDetailsScreen : AccountListRoutes(WITHDRAW_NAVIGATION_KEY)


    private companion object {
        private const val ACCOUNT_LIST_NAVIGATION_KEY = "account-list"
        private const val ACCOUNT_DETAILS_NAVIGATION_KEY = "account_details"
        private const val WITHDRAW_NAVIGATION_KEY = "withdraw_key"
    }
}

fun NavController.navigateToWithdraw(id: String, currentBalance: String, accountCurrencyType: String) {
    navigate(
        WithdrawNavigation(
            accountId = id,
            currentBalance = currentBalance,
            accountCurrencyType = accountCurrencyType,
        )
    )
}

fun NavController.navigateToDeposit(id: String, accountCurrencyType: String) {
    navigate(DepositNavigation(accountId = id, accountCurrencyType = accountCurrencyType))
}

fun NavController.navigateToAccountDetails(
    id: Int,
    title: String,
    isClosed: Boolean,
    moneyAmount: String,
    color: Color,
    accountCurrencyType: String,
) {
    navigate(
        AccountNavigation(
            id = id,
            title = title,
            isClosed = isClosed,
            moneyAmount = moneyAmount,
            red = color.red,
            green = color.green,
            blue = color.blue,
            accountCurrencyType = accountCurrencyType,
        )
    )
}

fun NavController.navigateBackToAccountList(graphId: Int) {
    navigate(AccountListRoutes.AccountListScreen.route) {
        popUpTo(graphId) { inclusive = true }
    }
}

fun NavGraphBuilder.buildAccountListsGraph(
    navigateToWithdraw: (String, String, String) -> Unit,
    onNavigateToDetails: (Int, String, Boolean, String, Color, String) -> Unit,
    onNavigateToDeposit: (String, String) -> Unit,
    onNavigateBackToAccountList: () -> Unit,
    onNavigateBackToDetails: () -> Unit,
    onTakeMoneyButtonTap: () -> Unit,
) {
    composable(route = AccountListRoutes.AccountListScreen.route) {
        AccountListScreen(
            viewModel = hiltViewModel(),
            onNavigateToAccountDetails = onNavigateToDetails,
            onTakeMoneyButtonTap = onTakeMoneyButtonTap,
        )
    }
    composable<AccountNavigation> { backStackEntry ->
        val account: AccountNavigation = backStackEntry.toRoute()
        val color = Color(account.red, account.green, account.blue)
        AccountDetailsScreen(
            id = account.id,
            isClosed = account.isClosed,
            title = account.title,
            accountCurrencyType = account.accountCurrencyType,
            color = color,
            viewModel = hiltViewModel(),
            onClickOnWithdraw = navigateToWithdraw,
            onClickOnDeposit = onNavigateToDeposit,
            onNavigateBackToAccountList = onNavigateBackToAccountList,
        )
    }
    composable<WithdrawNavigation> { backStackEntry ->
        val direction: WithdrawNavigation = backStackEntry.toRoute()
        WithdrawScreen(
            accountId = direction.accountId,
            currentBalance = direction.currentBalance,
            viewModel = hiltViewModel(),
            onNavigateToDetails = onNavigateBackToDetails,
            accountCurrencyType = direction.accountCurrencyType,
        )
    }
    composable<DepositNavigation> { backStackEntry ->
        val direction: DepositNavigation = backStackEntry.toRoute()
        DepositScreen(
            accountId = direction.accountId,
            viewModel = hiltViewModel(),
            onNavigateToDetails = onNavigateBackToDetails,
            accountCurrencyType = direction.accountCurrencyType,
        )
    }
}