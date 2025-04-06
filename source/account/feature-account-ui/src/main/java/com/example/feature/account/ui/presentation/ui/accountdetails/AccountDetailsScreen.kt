@file:OptIn(ExperimentalLayoutApi::class)

package com.example.feature.account.ui.presentation.ui.accountdetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.feature.account.ui.R
import com.example.feature.account.ui.presentation.ui.transfer.externaltransfer.ExternalTransferCard
import kotlinx.coroutines.flow.collectLatest
import ru.qwerty.common_uikit.component.BankCardUi
import ru.qwerty.common_uikit.component.DepositTransactionCard
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.WithdrawTransactionCard
import ru.qwerty.common_uikit.component.button.AccentButtonChip
import ru.qwerty.common_uikit.component.button.HiddenAccountButton
import ru.qwerty.common_uikit.component.theme.Colors

/**
 * @author y.yangel
 */
@Composable
internal fun AccountDetailsScreen(
    id: Int,
    isClosed: Boolean,
    title: String,
    isHidden: Boolean,
    accountCurrencyType: String,
    color: Color,
    viewModel: AccountDetailsViewModel,
    modifier: Modifier = Modifier,
    onClickOnWithdraw: (String, String, String) -> Unit,
    onClickOnDeposit: (String, String) -> Unit,
    onNavigateBackToAccountList: () -> Unit,
    onBetweenAccountsTap: (String, String, String) -> Unit,
    onAccountNumberTap: (String, String, String) -> Unit,
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
        accountCurrencyType = accountCurrencyType,
        onAccountNumberTap = onAccountNumberTap,
        onBetweenAccountsTap = onBetweenAccountsTap,
        isHidden = isHidden,
        onHiddenClicked = { viewModel.setHiddenAccount(isHidden = it, accountId = id) }
    )
}

@Composable
private fun AccountDetailsScreenInternal(
    state: AccountDetailsState,
    accountId: String,
    moneyAmount: String,
    accountCurrencyType: String,
    title: String,
    color: Color,
    isClosed: Boolean,
    isHidden: Boolean,
    modifier: Modifier = Modifier,
    onClickOnWithdraw: (String, String, String) -> Unit,
    onClickOnDeposit: (String, String) -> Unit,
    onCloseAccountTap: (String) -> Unit,
    onBetweenAccountsTap: (String, String, String) -> Unit,
    onAccountNumberTap: (String, String, String) -> Unit,
    onHiddenClicked: (Boolean) -> Unit,
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
                accountCurrencyType = accountCurrencyType,
                cardNumber = accountId.toInt(),
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
                accountId = accountId,
                accountCurrencyType = accountCurrencyType,
                onBetweenAccountsTap = onBetweenAccountsTap,
                onAccountNumberTap = onAccountNumberTap,
                onHiddenClicked = onHiddenClicked,
                isHidden = isHidden,
            )
        }
        when {
            state.error -> item { AccountDetailsErrorScreen() }

            state.isLoading -> item { AccountDetailsLoadingScreen() }

            state.transactionsUi.transactions.isNotEmpty() -> {
                items(state.transactionsUi.transactions) { item ->
                    when (item.type) {
                        "Deposit" -> DepositTransactionCard(
                            transactionDeposit = item.moneyAmount.toString(),
                            code = item.code ?: "Rub"
                        )

                        "Withdraw" -> WithdrawTransactionCard(
                            transactionWithdraw = item.moneyAmount.toString(),
                            code = item.code ?: "Rub"
                        )
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
    isClosed: Boolean,
    moneyAmount: String,
    isHidden: Boolean,
    accountCurrencyType: String,
    modifier: Modifier = Modifier,
    onDepositTap: (String, String) -> Unit = { _, _ -> },
    onWithdrawTap: (String, String, String) -> Unit = { _, _, _ -> },
    onCloseAccountTap: (String) -> Unit = {},
    onBetweenAccountsTap: (String, String, String) -> Unit,
    onAccountNumberTap: (String, String, String) -> Unit,
    onHiddenClicked: (Boolean) -> Unit = {},
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
        Column {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.Center,
            ) {
                AccentButtonChip(
                    text = stringResource(R.string.deposit),
                    onClick = { onDepositTap(accountId, accountCurrencyType) }
                )
                AccentButtonChip(
                    text = stringResource(R.string.withdraw),
                    onClick = { onWithdrawTap(accountId, moneyAmount, accountCurrencyType) })
                AccentButtonChip(
                    text = stringResource(R.string.close),
                    onClick = { onCloseAccountTap(accountId) },
                    containerColor = Colors.PeppyPeacock,
                )
                HiddenAccountButton(
                    icon = painterResource(R.drawable.eye_closed),
                    hiddenIcon = painterResource(R.drawable.eye_open),
                    isHidden = isHidden,
                    onClick = onHiddenClicked,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    ExternalTransferCard(
                        "Между счетами",
                        accountId = accountId,
                        icon = ImageVector.vectorResource(R.drawable.transfer_between),
                        accountMoneyAmount = moneyAmount,
                        accountCurrencyType = accountCurrencyType,
                        onItemClicked = onBetweenAccountsTap,
                    )
                    ExternalTransferCard(
                        "По номеру карты",
                        accountId = accountId,
                        icon = ImageVector.vectorResource(R.drawable.transfer_by_number),
                        accountMoneyAmount = moneyAmount,
                        onItemClicked = onAccountNumberTap,
                        accountCurrencyType = accountCurrencyType,
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun AccountDetailsActionsPreview() {
    AccountDetailsActions(
        accountId = "1",
        isClosed = false,
        moneyAmount = "50000",
        accountCurrencyType = "Usd",
        onBetweenAccountsTap = { _, _, _ -> },
        onAccountNumberTap = { _, _, _ -> },
        isHidden = false,
    )
}