package ru.qwerty.feature.credits.ui.presentation.composable.loans

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.LoanCard
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.common_uikit.component.theme.Colors
import ru.qwerty.common_uikit.component.theme.Colors.SpanishGrey
import ru.qwerty.feature.credits.api.domain.LoanStatus
import ru.qwerty.feature.credits.ui.presentation.composable.loans.viewmodel.LoanListState
import ru.qwerty.feature.credits.ui.presentation.composable.loans.viewmodel.LoanUi

/**
 * @author y.yangel
 */
@Composable
internal fun LoansListContentScreen(
    state: LoanListState,
    modifier: Modifier = Modifier,
    onLoanTap: (String) -> Unit,
    onGetLoanTap: () -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {
        item { SectionTitle(text = "Список кредитов") }
        DisplayLoan(
            state.loanList.filter { it.status == LoanStatus.ACTIVE || it.status == LoanStatus.REQUESTED },
            onLoanTap = onLoanTap
        )
        item {
            AccentButton(
                text = "Взять кредит",
                modifier = Modifier.padding(top = 8.dp),
                onClick = onGetLoanTap,
            )
        }
        item { SectionTitle(text = "Закрытые кредиты") }
        DisplayLoan(
            state.loanList.filterNot { it.status == LoanStatus.ACTIVE || it.status == LoanStatus.REQUESTED },
            onLoanTap = onLoanTap
        )
    }
}

private fun LazyListScope.DisplayLoan(
    loans: List<LoanUi>,
    onLoanTap: (String) -> Unit,
) {
    items(loans) { loan ->
        val isLoanClickable = loan.status == LoanStatus.ACTIVE || loan.status == LoanStatus.CLOSED
        val modifier = if (isLoanClickable) Modifier.clickable { onLoanTap(loan.id) } else Modifier
        LoanCard(
            modifier = modifier,
            status = loan.status.name,
            debtAmount = loan.debtAmount,
            nextPaymentAmount = loan.nextPaymentAmount,
            nextPaymentDate = loan.nextPaymentDate,
        )
    }
}

@Composable
fun SectionTitle(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = TextStyle(
            fontFamily = LatoFont,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Colors.JetBlack,
            letterSpacing = 1.sp,
        )
    )
}

@Composable
internal fun LoansListEmptyScreen(
    modifier: Modifier = Modifier,
    onGetLoanTap: () -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item { SectionTitle(text = "Список кредитов") }
        item {
            AccentButton(
                text = "Взять кредит",
                modifier = Modifier.padding(top = 8.dp),
                onClick = onGetLoanTap,
            )
        }
        item {
            Text(
                text = "Здесь будут показываться ваши кредиты",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontFamily = LatoFont,
                    fontWeight = FontWeight.W400,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = SpanishGrey,
                    letterSpacing = 1.sp,
                )
            )
        }
    }
}