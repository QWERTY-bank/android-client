package ru.qwerty.impl.data.mapper

import ru.qwerty.feature.credits.api.domain.Loan
import ru.qwerty.feature.credits.api.domain.LoanStatus
import ru.qwerty.impl.data.api.LoanResultApi
import ru.qwerty.impl.data.api.LoanStatusApi

/**
 * @author y.yangel
 */
internal fun LoanResultApi.toDomain(): Loan {
    return Loan(
        id = id,
        status = status.toDomain(),
        debtAmount = debtAmount,
        nextPaymentAmount = nextPaymentAmount,
        nextPaymentDateOnly = nextPaymentDateOnly
    )
}

internal fun LoanStatusApi.toDomain(): LoanStatus {
    return when (this) {
        LoanStatusApi.REQUESTED -> LoanStatus.REQUESTED
        LoanStatusApi.ACTIVE -> LoanStatus.ACTIVE
        LoanStatusApi.CLOSED -> LoanStatus.CLOSED
        LoanStatusApi.CANCELED -> LoanStatus.CANCELED
    }
}
