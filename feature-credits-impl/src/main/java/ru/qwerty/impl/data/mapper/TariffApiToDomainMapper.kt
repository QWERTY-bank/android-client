package ru.qwerty.impl.data.mapper

import ru.qwerty.feature.credits.api.domain.CreditType
import ru.qwerty.feature.credits.api.domain.LoanDetails
import ru.qwerty.feature.credits.api.domain.NextPayment
import ru.qwerty.feature.credits.api.domain.PaymentHistory
import ru.qwerty.feature.credits.api.domain.Tariff
import ru.qwerty.impl.data.api.CreditTypeApi
import ru.qwerty.impl.data.api.LoanResponseApi
import ru.qwerty.impl.data.api.NextPaymentApi
import ru.qwerty.impl.data.api.PaymentHistoryApi
import ru.qwerty.impl.data.api.TariffsApi

/**
 * @author y.yangel
 */
internal fun LoanResponseApi.toDomain(): LoanDetails {
    return LoanDetails(
        id = id,
        status = status,
        tariff = tariff.toDomain(),
        debtAmount = debtAmount,
        takingDate = takingDate,
        nextPayments = nextPayments.map { it.toDomain() },
        paymentHistory = paymentHistory.map { it.toDomain() },
    )
}

internal fun NextPaymentApi.toDomain(): NextPayment {
    return NextPayment(
        paymentAmount = paymentAmount,
        paymentDateOnly = paymentDateOnly
    )
}

internal fun PaymentHistoryApi.toDomain(): PaymentHistory {
    return PaymentHistory(
        paymentAmount = paymentAmount,
        paymentDateTime = paymentDateTime,
        paymentStatus = paymentStatus,
        paymentType = paymentType,
    )
}

internal fun TariffsApi.toDomain(): Tariff {
    return Tariff(
        id = id,
        name = name,
        interestRate = interestRate,
        interestRateType = interestRateType.toDomain(),
        minPeriodDays = minPeriodDays,
        maxPeriodDays = maxPeriodDays,
    )
}

internal fun CreditTypeApi.toDomain(): CreditType {
    return when (this) {
        CreditTypeApi.Annual -> CreditType.Annual
        CreditTypeApi.Monthly -> CreditType.Monthly
        CreditTypeApi.Daytime -> CreditType.Daytime
    }
}