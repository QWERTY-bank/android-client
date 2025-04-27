package ru.qwerty.bank

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await
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
import ru.qwerty.ui.navigation.navigateToLogin

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true

        val uri = intent?.data
        val code = uri?.getQueryParameter("code")?.replace(" ", "+")
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

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermissions = ContextCompat.checkSelfPermission(
                /* context = */ this,
                /* permission = */ Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermissions) {
                ActivityCompat.requestPermissions(
                    /* activity = */ this,
                    /* permissions = */ arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    /* requestCode = */ 0,
                )
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
                onNavigateToDetails = { id,
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