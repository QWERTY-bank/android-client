package ru.qwerty.impl.data.api

import kotlinx.serialization.Serializable

/**
 * @author y.yangel
 */
@Serializable
internal class CreateLoanRequest(
    val key: String,
    val accountId: Int,
    val tariffId: String,
    val periodDays: Int,
    val loanAmount: Double,
)

@Serializable
internal data class LoanResponseApi(
    val id: String,
    val status: String,
    val tariff: TariffsApi,
    val debtAmount: Double,
    val takingDate: String,
    val nextPayments: List<NextPaymentApi>,
    val paymentHistory: List<PaymentHistoryApi>,
)

@Serializable
internal data class NextPaymentApi(
    val paymentAmount: Double,
    val paymentDateOnly: String,
)

@Serializable
internal data class PaymentHistoryApi(
    val paymentAmount: Double,
    val paymentDateTime: String,
    val paymentStatus: String,
    val paymentType: String,
)