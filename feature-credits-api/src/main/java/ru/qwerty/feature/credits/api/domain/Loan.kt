package ru.qwerty.feature.credits.api.domain

/**
 * @author y.yangel
 */
class Loan(
    val id: String,
    val status: LoanStatus,
    val debtAmount: Double,
    val nextPaymentAmount: Double,
    val nextPaymentDateOnly: String
)

enum class LoanStatus {
    REQUESTED,
    ACTIVE,
    CLOSED,
    CANCELED
}

data class LoanDetails(
    val id: String,
    val status: String,
    val tariff: Tariff,
    val debtAmount: Double,
    val takingDate: String,
    val nextPayments: List<NextPayment>,
    val paymentHistory: List<PaymentHistory>
)

data class NextPayment(
    val paymentAmount: Double,
    val paymentDateOnly: String
)

data class PaymentHistory(
    val paymentAmount: Double,
    val paymentDateTime: String,
    val paymentStatus: String
)