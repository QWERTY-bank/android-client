package ru.qwerty.bank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.feature.account.ui.presentation.ui.navigation.buildAccountListsGraph
import com.example.feature.account.ui.presentation.ui.navigation.navigateBackToAccountList
import com.example.feature.account.ui.presentation.ui.navigation.navigateToAccountDetails
import com.example.feature.account.ui.presentation.ui.navigation.navigateToDeposit
import com.example.feature.account.ui.presentation.ui.navigation.navigateToWithdraw
import dagger.hilt.android.AndroidEntryPoint
import ru.qwerty.bank.ui.theme.QwertybankTheme
import ru.qwerty.feature.credits.ui.presentation.navigation.buildCreditsFeatureGraph
import ru.qwerty.feature.credits.ui.presentation.navigation.navigateToTariffListScreen
import ru.qwerty.ui.navigation.AccountRoutes
import ru.qwerty.ui.navigation.buildAccountGraph
import ru.qwerty.ui.navigation.navigateToAccountList
import ru.qwerty.ui.navigation.navigateToLogin

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true
        setContent {
            QwertybankTheme {
                ComposeNavGraph()
            }
        }
    }
}

@Composable
internal fun ComposeNavGraph(modifier: Modifier = Modifier) {
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
            buildCreditsFeatureGraph()
            buildAccountGraph(
                onNavigateToAccountList = { navController.navigateToAccountList() },
                onNavigateToLogin = { navController.navigateToLogin() }
            )
            buildAccountListsGraph(
                navigateToWithdraw = { id, currentBalance ->
                    navController.navigateToWithdraw(
                        id = id,
                        currentBalance = currentBalance
                    )
                },
                onNavigateToDetails = { id,
                                        title,
                                        isClosed,
                                        moneyAmount,
                                        color ->
                    navController.navigateToAccountDetails(
                        id = id,
                        title = title,
                        isClosed = isClosed,
                        moneyAmount = moneyAmount,
                        color = color
                    )
                },
                onNavigateBackToDetails = { navController.popBackStack() },
                onNavigateBackToAccountList = {
                    navController.navigateBackToAccountList(
                        graphId = navController.graph.id,
                    )
                },
                onNavigateToDeposit = { accountId -> navController.navigateToDeposit(accountId) },
                onTakeMoneyButtonTap = { navController.navigateToTariffListScreen() }
            )
        }
    }
}