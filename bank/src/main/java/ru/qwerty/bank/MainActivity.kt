package ru.qwerty.bank

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.feature.account.ui.presentation.ui.navigation.buildAccountListsGraph
import com.example.feature.account.ui.presentation.ui.navigation.navigateBackToAccountList
import com.example.feature.account.ui.presentation.ui.navigation.navigateToAccountDetails
import com.example.feature.account.ui.presentation.ui.navigation.navigateToDeposit
import com.example.feature.account.ui.presentation.ui.navigation.navigateToWithdraw
import com.example.feature.account.ui.presentation.ui.transfer.navigation.navigateToSelfTransferActionScreen
import com.example.feature.account.ui.presentation.ui.transfer.navigation.navigateToSelfTransferScreen
import com.example.feature.account.ui.presentation.ui.transfer.navigation.navigateToTransferActionScreen
import com.example.feature.account.ui.presentation.ui.transfer.navigation.selfTransferGraph
import dagger.hilt.android.AndroidEntryPoint
import okio.ByteString.Companion.encodeUtf8
import ru.qwerty.bank.ui.theme.QwertybankTheme
import ru.qwerty.feature.credits.ui.presentation.navigation.buildCreditsFeatureGraph
import ru.qwerty.feature.credits.ui.presentation.navigation.navigateBackToLoansListScreen
import ru.qwerty.feature.credits.ui.presentation.navigation.navigateToApplyLoansDirection
import ru.qwerty.feature.credits.ui.presentation.navigation.navigateToChooseAccountScreen
import ru.qwerty.feature.credits.ui.presentation.navigation.navigateToDepositForLoanScreen
import ru.qwerty.feature.credits.ui.presentation.navigation.navigateToLoanDetailsScreen
import ru.qwerty.feature.credits.ui.presentation.navigation.navigateToLoansListScreen
import ru.qwerty.feature.credits.ui.presentation.navigation.navigateToLoansReduceChooseAccount
import ru.qwerty.feature.credits.ui.presentation.navigation.navigateToTariffListScreen
import ru.qwerty.ui.navigation.AccountRoutes
import ru.qwerty.ui.navigation.buildAccountGraph
import ru.qwerty.ui.navigation.navigateToAccountList
import ru.qwerty.ui.navigation.navigateToLogin
import java.net.URLDecoder

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true

        val uri = intent?.data
        val code = uri?.getQueryParameter("code")?.let { Uri.decode(it) }
        Log.d("qwerty", "code = $code")

        setContent {
            QwertybankTheme {
                val codeState = remember { mutableStateOf(code) }

                LaunchedEffect(code) {
                    code?.let {
                        codeState.value = it
                    }
                }

                ComposeNavGraph(code = codeState.value)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}

@Composable
internal fun ComposeNavGraph(modifier: Modifier = Modifier, code: String? = null) {
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier
            .navigationBarsPadding()
            .statusBarsPadding(),
    ) {
        NavHost(
            modifier = modifier.padding(it),
            navController = navController,
            startDestination = AccountRoutes.SplashScreen.route,
        ) {
            buildCreditsFeatureGraph(
                onGetLoanClick = { navController.navigateToTariffListScreen() },
                onTariffTap = { tariffId, minDays, maxDays ->
                    navController.navigateToApplyLoansDirection(
                        tariffId = tariffId,
                        minPeriodDays = minDays,
                        maxPeriodDays = maxDays,
                    )
                },
                onContinueButtonTap = { amount, daysAmount, tariffId ->
                    navController.navigateToChooseAccountScreen(
                        amount = amount,
                        daysAmount = daysAmount,
                        tariffId = tariffId,
                    )
                },
                onNavigateBackToLoans = { navController.navigateBackToLoansListScreen(navController.graph.id) },
                onNavigateBackToAccountList = {
                    navController.navigateBackToAccountList(
                        graphId = navController.graph.id,
                    )
                },
                onNavigateToLoanDetails = { loanId ->
                    navController.navigateToLoanDetailsScreen(loanId)
                },
                onAccountTap = { value, accountId, creditId ->
                    navController.navigateToDepositForLoanScreen(
                        accountMoneyAmount = value,
                        accountId = accountId,
                        creditId = creditId,
                    )
                },
                onPayClick = { loanId -> navController.navigateToLoansReduceChooseAccount(loanId) },
            )
            buildAccountGraph(
                onNavigateToAccountList = {
                    navController.navigateBackToAccountList(
                        graphId = navController.graph.id,
                    )
                },
                onNavigateToLogin = { navController.navigateToLogin() },
                code = code,
            )
            selfTransferGraph(
                onAccountTap = { fromAccountId, toAccountId, moneyAmount, code ->
                    navController.navigateToSelfTransferActionScreen(
                        fromAccountId = fromAccountId,
                        toAccountId = toAccountId,
                        moneyAmount = moneyAmount,
                        code = code,
                    )
                },
                navigateToMainScreen = {
                    navController.navigateBackToAccountList(
                        graphId = navController.graph.id,
                    )
                }
            )
            buildAccountListsGraph(
                navigateToWithdraw = { id, currentBalance, accountCurrencyType ->
                    navController.navigateToWithdraw(
                        id = id,
                        currentBalance = currentBalance,
                        accountCurrencyType = accountCurrencyType,
                    )
                },
                onNavigateToDetails = {
                        id,
                        title,
                        isClosed,
                        moneyAmount,
                        color,
                        accountCurrencyType,
                        isHidden ->
                    navController.navigateToAccountDetails(
                        id = id,
                        title = title,
                        isClosed = isClosed,
                        moneyAmount = moneyAmount,
                        color = color,
                        accountCurrencyType = accountCurrencyType,
                        isHidden = isHidden,
                    )
                },
                onNavigateBackToDetails = { navController.popBackStack() },
                onNavigateBackToAccountList = {
                    navController.navigateBackToAccountList(
                        graphId = navController.graph.id,
                    )
                },
                onNavigateToDeposit = { accountId, accountCurrencyType ->
                    navController.navigateToDeposit(
                        accountId,
                        accountCurrencyType
                    )
                },
                onTakeMoneyButtonTap = { navController.navigateToLoansListScreen() },
                onBetweenAccountsTap = { fromAccountId, moneyAmount, code ->
                    navController.navigateToSelfTransferScreen(fromAccountId, moneyAmount, code)
                },
                onAccountNumberTap = { fromAccountId, moneyAmount, code ->
                    navController.navigateToTransferActionScreen(fromAccountId, moneyAmount, code)
                },
            )
        }
    }
}