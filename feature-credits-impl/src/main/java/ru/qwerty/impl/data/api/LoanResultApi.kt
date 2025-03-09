package ru.qwerty.impl.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author y.yangel
 */
@Serializable
internal class LoanApiResponse(
    val results: List<LoanResultApi>
)

@Serializable
internal class LoanResultApi(
    val id: String,
    val status: LoanStatusApi,
    val debtAmount: Double,
    val nextPaymentAmount: Double,
    val nextPaymentDateOnly: String
)

@Serializable
internal enum class LoanStatusApi {

    @SerialName("Requested")
    REQUESTED,

    @SerialName("Active")
    ACTIVE,

    @SerialName("Closed")
    CLOSED,

    @SerialName("Canceled")
    CANCELED
}