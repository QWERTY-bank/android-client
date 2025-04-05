package com.example.feature.account.ui.presentation.ui.transfer.navigation

import androidx.compose.runtime.Immutable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.feature.account.ui.presentation.ui.transfer.externaltransfer.ExternalTransferScreen
import com.example.feature.account.ui.presentation.ui.transfer.selftransfer.SelfTransferActionScreen
import com.example.feature.account.ui.presentation.ui.transfer.selftransfer.SelfTransferScreen
import kotlinx.serialization.Serializable

@Serializable
@Immutable
internal data class SelfTransferDirection(
    val fromAccountId: String,
    val moneyAmount: String,
    val code: String
)

@Serializable
@Immutable
internal data class SelfTransferActionDirection(
    val fromAccountId: String,
    val toAccountId: String,
    val moneyAmount: String,
    val code: String,
)

@Serializable
@Immutable
internal data class TransferActionDirection(
    val fromAccountId: String,
    val moneyAmount: String,
    val code: String,
)

fun NavController.navigateToSelfTransferScreen(
    fromAccountId: String,
    moneyAmount: String,
    code: String
) {
    navigate(
        SelfTransferDirection(
            fromAccountId = fromAccountId,
            moneyAmount = moneyAmount,
            code = code
        )
    )
}

fun NavController.navigateToSelfTransferActionScreen(
    fromAccountId: String,
    toAccountId: String,
    moneyAmount: String,
    code: String
) {
    navigate(
        SelfTransferActionDirection(
            fromAccountId = fromAccountId,
            toAccountId = toAccountId,
            moneyAmount = moneyAmount,
            code = code,
        )
    )
}


fun NavController.navigateToTransferActionScreen(
    fromAccountId: String,
    moneyAmount: String,
    code: String
) {
    navigate(
        TransferActionDirection(
            fromAccountId = fromAccountId,
            moneyAmount = moneyAmount,
            code = code,
        )
    )
}

fun NavGraphBuilder.selfTransferGraph(
    onAccountTap: (String, String, String, String) -> Unit,
    navigateToMainScreen: () -> Unit,
) {
    composable<SelfTransferDirection> { stack ->
        val direction: SelfTransferDirection = stack.toRoute()
        SelfTransferScreen(
            viewModel = hiltViewModel(),
            moneyAmount = direction.moneyAmount,
            fromAccountId = direction.fromAccountId,
            onAccountTap = onAccountTap,
            accountCurrencyType = direction.code,
        )
    }
    composable<SelfTransferActionDirection> { stack ->
        val direction: SelfTransferActionDirection = stack.toRoute()
        SelfTransferActionScreen(
            fromAccountId = direction.fromAccountId,
            toAccountId = direction.toAccountId,
            moneyAmountOnAccount = direction.moneyAmount,
            accountCurrencyType = direction.code,
            viewModel = hiltViewModel(),
            onSuccessButtonClicked = navigateToMainScreen,
        )
    }
    composable<TransferActionDirection> { stack ->
        val direction: TransferActionDirection = stack.toRoute()
        ExternalTransferScreen(
            fromAccountId = direction.fromAccountId,
            moneyAmount = direction.moneyAmount,
            code = direction.code,
            onNavigateToSelfActions = onAccountTap
        )
    }
}