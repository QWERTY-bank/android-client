@file:OptIn(ExperimentalLayoutApi::class)

package com.example.feature.account.ui.presentation.ui.accountdetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.feature.account.ui.R
import kotlinx.coroutines.flow.collectLatest
import ru.qwerty.common_uikit.component.BankCardUi
import ru.qwerty.common_uikit.component.DepositTransactionCard
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.WithdrawTransactionCard
import ru.qwerty.common_uikit.component.button.AccentButtonChip
import ru.qwerty.common_uikit.component.theme.Colors

/**
 * @author y.yangel
 */
@Composable
internal fun AccountDetailsScreen(
    id: Int,
    isClosed: Boolean,
    title: String,
    moneyAmount: String,
    color: Color,
    viewModel: AccountDetailsViewModel,
    modifier: Modifier = Modifier,
    onClickOnWithdraw: (String, String) -> Unit,
    onClickOnDeposit: (String) -> Unit,
    onNavigateBackToAccountList: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadAccountTransactions(id = id)
        viewModel.news.collectLatest {
            onNavigateBackToAccountList()
        }
    }

    BackHandler {
        onNavigateBackToAccountList()
    }

    AccountDetailsScreenInternal(
        state = state,
        modifier = modifier,
        moneyAmount = state.accountBalance ?: "...",
        title = title,
        color = color,
        isClosed = isClosed,
        onClickOnWithdraw = onClickOnWithdraw,
        onClickOnDeposit = onClickOnDeposit,
        onCloseAccountTap = viewModel::closeAccountById,
        accountId = id.toString(),
    )
}

@Composable
private fun AccountDetailsScreenInternal(
    state: AccountDetailsState,
    accountId: String,
    moneyAmount: String,
    title: String,
    color: Color,
    isClosed: Boolean,
    modifier: Modifier = Modifier,
    onClickOnWithdraw: (String, String) -> Unit,
    onClickOnDeposit: (String) -> Unit,
    onCloseAccountTap: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(space = 24.dp)
    ) {
        item {
            BankCardUi(
                balance = moneyAmount,
                cardTitle = title,
                baseColor = color,
            )
        }
        item {
            AccountDetailsActions(
                modifier = modifier,
                moneyAmount = state.accountBalance ?: "...",
                isClosed = isClosed,
                onWithdrawTap = onClickOnWithdraw,
                onCloseAccountTap = onCloseAccountTap,
                onDepositTap = onClickOnDeposit,
                accountId = accountId
            )
        }
        when {
            state.error -> item { AccountDetailsErrorScreen() }

            state.isLoading -> item { AccountDetailsLoadingScreen() }

            state.transactionsUi.transactions.isNotEmpty() -> {
                items(state.transactionsUi.transactions) { item ->
                    when (item.type) {
                        "Deposit" -> DepositTransactionCard(transactionDeposit = item.moneyAmount.toString())
                        "Withdraw" -> WithdrawTransactionCard(transactionWithdraw = item.moneyAmount.toString())
                    }
                }
            }

            state.transactionsUi.transactions.isEmpty() -> item {
                AccountDetailsEmptyScreen()
            }
        }
    }
}

@Composable
private fun AccountDetailsActions(
    accountId: String,
    modifier: Modifier,
    isClosed: Boolean,
    moneyAmount: String,
    onDepositTap: (String) -> Unit = {},
    onWithdrawTap: (String, String) -> Unit,
    onCloseAccountTap: (String) -> Unit,
) {
    if (isClosed) {
        Text(
            text = stringResource(R.string.account_closed),
            modifier = modifier.fillMaxWidth(),
            style = TextStyle(
                fontFamily = LatoFont,
                fontWeight = FontWeight.W400,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                letterSpacing = 1.sp,
                color = Colors.PeppyPeacock
            )
        )
    } else {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.Center,
        ) {
            AccentButtonChip(
                text = stringResource(R.string.deposit),
                onClick = { onDepositTap(accountId) }
            )
            AccentButtonChip(
                text = stringResource(R.string.withdraw),
                onClick = { onWithdrawTap(accountId, moneyAmount) })
            AccentButtonChip(
                text = stringResource(R.string.close),
                onClick = { onCloseAccountTap(accountId) },
                containerColor = Colors.PeppyPeacock,
            )
        }
    }
}
