package ru.qwerty.feature.credits.ui.presentation.composable.loandetails.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.qwerty.common_uikit.component.LoanDetailsCard
import ru.qwerty.common_uikit.component.NextPaymentCard
import ru.qwerty.common_uikit.component.PaymentHistoryCard
import ru.qwerty.common_uikit.component.TariffCard
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.feature.credits.api.domain.CreditType
import ru.qwerty.feature.credits.api.domain.LoanDetails
import ru.qwerty.feature.credits.api.domain.NextPayment
import ru.qwerty.feature.credits.api.domain.PaymentHistory
import ru.qwerty.feature.credits.api.domain.Tariff
import ru.qwerty.feature.credits.ui.presentation.composable.loandetails.viewmodel.LoanDetailsState
import ru.qwerty.feature.credits.ui.presentation.composable.loandetails.viewmodel.LoanDetailsUi
import ru.qwerty.feature.credits.ui.presentation.composable.loandetails.viewmodel.toUi
import ru.qwerty.feature.credits.ui.presentation.composable.loans.SectionTitle

/**
 * @author y.yangel
 */
@Composable
internal fun LoanDetailsScreenContent(
    state: LoanDetailsState,
    loanDetails: LoanDetailsUi,
    modifier: Modifier = Modifier,
    onPayClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item { SectionTitle(text = "Ваш кредитный рейтинг ${state.rating} из ${state.maxRating}") }
        item { SectionTitle(text = "Детали кредита") }
        item {
            LoanDetailsCard(
                status = loanDetails.status,
                debtAmount = loanDetails.debtAmount,
                date = loanDetails.takingDate,
            )
        }
        item {
            AccentButton(text = "Погасить досрочно", onClick = { onPayClick(loanDetails.id) })
        }
        item { SectionTitle(text = "Тариф") }
        item {
            TariffCard(
                name = loanDetails.tariff.name,
                interestRate = loanDetails.tariff.interestRate,
                interestRateType = loanDetails.tariff.interestRateType.toString(),
                minPeriodDays = loanDetails.tariff.minPeriodDays.toString(),
                maxPeriodDays = loanDetails.tariff.maxPeriodDays.toString(),
            )
        }

        if (loanDetails.paymentHistory.isEmpty()) {
            item { SectionTitle(text = "Здесь будет история ваших погашений") }
        } else {
            item { SectionTitle(text = "Погашения") }
            items(loanDetails.paymentHistory) { paymentHistory ->
                PaymentHistoryCard(
                    paymentAmount = paymentHistory.paymentAmount,
                    paymentDate = paymentHistory.paymentDate,
                    paymentStatus = paymentHistory.paymentStatus,
                    paymentType = paymentHistory.paymentType,
                )
            }
        }

        if (loanDetails.status == "Active") {
            item { SectionTitle(text = "Будущие оплаты") }
            items(loanDetails.nextPayments) { nextPayments ->
                if (nextPayments.paymentAmount.toDouble() > 0) {
                    NextPaymentCard(
                        nextPaymentAmount = nextPayments.paymentAmount,
                        nextPaymentDate = nextPayments.paymentDate,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoanDetailsScreenContentPreview() {
    LoanDetailsScreenContent(
        loanDetails = loanDetails,
        onPayClick = {},
        state = LoanDetailsState(),
    )
}

internal val loanDetails = LoanDetails(
    id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    status = "ACTIVE",
    tariff = Tariff(
        id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        name = "Standard",
        interestRate = 5.0,
        interestRateType = CreditType.Annual,
        minPeriodDays = 30,
        maxPeriodDays = 365
    ),
    debtAmount = 10000.0,
    takingDate = "2025-03-09",
    nextPayments = listOf(
        NextPayment(
            paymentAmount = 200.0,
            paymentDateOnly = "2025-04-09"
        ),
        NextPayment(
            paymentAmount = 200.0,
            paymentDateOnly = "2025-04-09"
        ),
    ),
    paymentHistory = listOf(
        PaymentHistory(
            paymentAmount = 200.0,
            paymentDateTime = "2025-03-09T12:00:00Z",
            paymentStatus = "Conducted",
            paymentType = "status",
        )
    )
).toUi()