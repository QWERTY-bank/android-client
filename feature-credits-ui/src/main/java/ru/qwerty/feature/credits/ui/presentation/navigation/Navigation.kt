package ru.qwerty.feature.credits.ui.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.qwerty.feature.credits.ui.presentation.composable.loandetails.screen.LoanDetailsScreen
import ru.qwerty.feature.credits.ui.presentation.composable.loans.LoansListScreen
import ru.qwerty.feature.credits.ui.presentation.composable.loansflow.ChooseAccountScreen
import ru.qwerty.feature.credits.ui.presentation.composable.loansflow.LoanApplyFlowScreen
import ru.qwerty.feature.credits.ui.presentation.composable.loansreduce.DepositForLoanScreen
import ru.qwerty.feature.credits.ui.presentation.composable.loansreduce.DepositLoadingScreen
import ru.qwerty.feature.credits.ui.presentation.composable.loansreduce.LoansReduceChooseAccountScreen
import ru.qwerty.feature.credits.ui.presentation.composable.tariff.TariffListScreen

/**
 * @author y.yangel
 */
sealed class CreditsNavigationRoute(val route: String) {
    data object TariffListScreenRoute : CreditsNavigationRoute(TariffListScreenRouteKey)
    data object LoansListScreenRoute : CreditsNavigationRoute(LoansListScreenRouteKey)

    private companion object {
        private const val TariffListScreenRouteKey = "TariffListScreenRouteKey"
        private const val LoansListScreenRouteKey = "LoansListScreenRouteKey"
    }
}

fun NavController.navigateToApplyLoansDirection(
    tariffId: String,
    minPeriodDays: String,
    maxPeriodDays: String,
) {
    navigate(
        ApplyLoansDirection(
            tariffId = tariffId,
            minPeriodDays = minPeriodDays.toInt(),
            maxPeriodDays = maxPeriodDays.toInt(),
        )
    )
}

fun NavController.navigateBackToLoansListScreen(graphId: Int) {
    navigate(CreditsNavigationRoute.LoansListScreenRoute.route) {
        popUpTo(graphId) { inclusive = true }
    }
}

fun NavController.navigateToTariffListScreen() {
    navigate(CreditsNavigationRoute.TariffListScreenRoute.route)
}

fun NavController.navigateToLoansListScreen() {
    navigate(CreditsNavigationRoute.LoansListScreenRoute.route)
}

fun NavController.navigateToChooseAccountScreen(
    amount: String,
    tariffId: String,
    daysAmount: String,
) {
    navigate(
        ChooseAccountDirection(
            amount = amount,
            daysAmount = daysAmount,
            tariffId = tariffId,
        )
    )
}

fun NavController.navigateToLoanDetailsScreen(loanId: String) {
    navigate(LoanDetailsDirection(loanId = loanId))
}

fun NavController.navigateToLoansReduceChooseAccount(loanId: String) {
    navigate(
        LoansReduceChooseAccountDirection(
            creditId = loanId
        )
    )
}

fun NavController.navigateToDepositForLoanScreen(
    accountMoneyAmount: String,
    accountId: Int,
    creditId: String,
) {
    navigate(
        DepositForLoanScreenDirection(
            accountMoneyAmount = accountMoneyAmount,
            accountId = accountId,
            creditId = creditId,
        )
    )
}

fun NavGraphBuilder.buildCreditsFeatureGraph(
    onGetLoanClick: () -> Unit = {},
    onTariffTap: (String, String, String) -> Unit,
    onContinueButtonTap: (String, String, String) -> Unit,
    onNavigateBackToLoans: () -> Unit = {},
    onNavigateBackToAccountList: () -> Unit,
    onNavigateToLoanDetails: (String) -> Unit,
    onAccountTap: (String, Int, String) -> Unit,
    onPayClick: (String) -> Unit,
) {
    composable(CreditsNavigationRoute.TariffListScreenRoute.route) {
        TariffListScreen(viewModel = hiltViewModel(), onTariffTap = onTariffTap)
    }
    composable(CreditsNavigationRoute.LoansListScreenRoute.route) {
        LoansListScreen(
            viewModel = hiltViewModel(),
            onGetLoanClick = onGetLoanClick,
            onNavigateBackToAccountList = onNavigateBackToAccountList,
            onLoanClick = onNavigateToLoanDetails,
        )
    }
    composable<ApplyLoansDirection> { backStackEntry ->
        val direction: ApplyLoansDirection = backStackEntry.toRoute()
        LoanApplyFlowScreen(
            minDays = direction.minPeriodDays,
            maxDays = direction.maxPeriodDays,
            tariffId = direction.tariffId,
            navigateToChooseAccount = onContinueButtonTap,
            viewModel = hiltViewModel()
        )
    }
    composable<ChooseAccountDirection> { backStackEntry ->
        val direction: ChooseAccountDirection = backStackEntry.toRoute()
        ChooseAccountScreen(
            tariffId = direction.tariffId,
            period = direction.daysAmount,
            amount = direction.amount,
            viewModel = hiltViewModel(),
            onNavigateBackToLoans = onNavigateBackToLoans,
        )
    }
    composable<LoanDetailsDirection> { backStackEntry ->
        val direction: LoanDetailsDirection = backStackEntry.toRoute()
        LoanDetailsScreen(
            loanId = direction.loanId,
            viewModel = hiltViewModel(),
            onNavigateBackToLoanList = onNavigateBackToLoans,
            onPayClick = onPayClick,
        )
    }
    composable<LoansReduceChooseAccountDirection> { backStackEntry ->
        val direction: LoansReduceChooseAccountDirection = backStackEntry.toRoute()
        LoansReduceChooseAccountScreen(
            viewModel = hiltViewModel(),
            creditId = direction.creditId,
            onAccountTap = onAccountTap,
        )
    }
    composable<DepositForLoanScreenDirection> { backStackEntry ->
        val direction: DepositForLoanScreenDirection = backStackEntry.toRoute()
        DepositForLoanScreen(
            accountId = direction.accountId,
            creditId = direction.creditId,
            accountMoneyAmount = direction.accountMoneyAmount,
            viewModel = hiltViewModel(),
            onNavigateBackToLoans = onNavigateBackToLoans,
        )
    }
}

val navigateToChooseAccountLambda: (String, String, String) -> Unit =
    { trafficId, minDaysParam, maxDaysParams ->
        navigateToChooseAccount(trafficId, minDaysParam, maxDaysParams)
    }

fun navigateToChooseAccount(
    trafficId: String = "123",
    daysAmount: String = "123",
    someOtherParam: String = "123"
) {
    println("trafficId: $trafficId, daysAmount: $daysAmount, someOtherParam: $someOtherParam")
}