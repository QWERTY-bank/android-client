package ru.qwerty.feature.credits.ui.presentation.composable.loans

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import ru.qwerty.feature.credits.api.domain.LoanStatus
import ru.qwerty.feature.credits.ui.presentation.composable.loans.viewmodel.LoanListState
import ru.qwerty.feature.credits.ui.presentation.composable.loans.viewmodel.LoanUi
import ru.qwerty.feature.credits.ui.presentation.composable.loans.viewmodel.LoansListViewModel

/**
 * @author y.yangel
 */
@Composable
internal fun LoansListScreen(
    modifier: Modifier = Modifier,
    viewModel: LoansListViewModel,
    onLoanClick: (String) -> Unit = {},
    onGetLoanClick: () -> Unit = {},
    onNavigateBackToAccountList: () -> Unit = {},
) {
    BackHandler {
        onNavigateBackToAccountList()
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    LoanListScreenInternal(
        state = state,
        modifier = modifier,
        onRetryButton = viewModel::loadLoans,
        onLoanTap = onLoanClick,
        onGetLoanClick = onGetLoanClick,
    )
}

@Composable
private fun LoanListScreenInternal(
    state: LoanListState,
    modifier: Modifier = Modifier,
    onRetryButton: () -> Unit = {},
    onLoanTap: (String) -> Unit = {},
    onGetLoanClick: () -> Unit = {},
) {
    when {
        state.loanList.isNotEmpty() -> LoansListContentScreen(
            state = state,
            onLoanTap = onLoanTap,
            onGetLoanTap = onGetLoanClick,
        )
        state.loanList.isEmpty() -> LoansListEmptyScreen(onGetLoanTap = onGetLoanClick)
        state.isError -> LoansListErrorScreen(modifier = modifier, onRetryClick = onRetryButton)
        state.isLoading -> LoansListLoadingScreen(modifier = modifier)

    }
}

@Preview
@Composable
private fun LoanListScreenInternalPreview() {
    LoanListScreenInternal(
        state = LoanListState(),
        onGetLoanClick = {},
    )
}

private val loanList = persistentListOf(
    LoanUi(
        id = "1",
        status = LoanStatus.REQUESTED,
        debtAmount = "5000",
        nextPaymentAmount = "0",
        nextPaymentDate = "2023-10-15"
    ),
    LoanUi(
        id = "2",
        status = LoanStatus.ACTIVE,
        debtAmount = "10000",
        nextPaymentAmount = "500",
        nextPaymentDate = "2023-10-15"
    ),
    LoanUi(
        id = "3",
        status = LoanStatus.CLOSED,
        debtAmount = "300000",
        nextPaymentAmount = "30000",
        nextPaymentDate = "2023-10-15"
    ),
    LoanUi(
        id = "4",
        status = LoanStatus.CANCELED,
        debtAmount = "1200000",
        nextPaymentAmount = "5000",
        nextPaymentDate = "2023-10-15"
    ),
    LoanUi(
        id = "5",
        status = LoanStatus.ACTIVE,
        debtAmount = "7500",
        nextPaymentAmount = "250",
        nextPaymentDate = "2023-11-01"
    )
)