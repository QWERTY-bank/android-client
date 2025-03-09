package ru.qwerty.feature.credits.ui.presentation.composable.loandetails.viewmodel

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ru.qwerty.feature.credits.api.domain.LoanDetails
import ru.qwerty.feature.credits.api.domain.NextPayment
import ru.qwerty.feature.credits.api.domain.PaymentHistory
import ru.qwerty.feature.credits.ui.mappers.toUi
import ru.qwerty.feature.credits.ui.presentation.composable.tariff.viewmodel.TariffUi

@Immutable
internal data class LoanDetailsUi(
    val id: String,
    val status: String,
    val tariff: TariffUi,
    val debtAmount: String,
    val takingDate: String,
    val nextPayments: ImmutableList<NextPaymentUi>,
    val paymentHistory: ImmutableList<PaymentHistoryUi>
)

@Immutable
internal data class NextPaymentUi(
    val paymentAmount: String,
    val paymentDate: String,
)

@Immutable
internal data class PaymentHistoryUi(
    val paymentAmount: String,
    val paymentDate: String,
    val paymentStatus: String,
)

internal fun LoanDetails.toUi(): LoanDetailsUi {
    return LoanDetailsUi(
        id = id,
        status = status,
        tariff = tariff.toUi(),
        debtAmount = "$debtAmount",
        takingDate = takingDate,
        nextPayments = nextPayments.map { it.toUi() }.toPersistentList(),
        paymentHistory = paymentHistory.map { it.toUi() }.toPersistentList(),
    )
}

internal fun NextPayment.toUi(): NextPaymentUi {
    return NextPaymentUi(
        paymentAmount = "$paymentAmount",
        paymentDate = paymentDateOnly,
    )
}

internal fun PaymentHistory.toUi(): PaymentHistoryUi {
    return PaymentHistoryUi(
        paymentAmount = "$paymentAmount",
        paymentDate = paymentDateTime,
        paymentStatus = paymentStatus
    )
}
