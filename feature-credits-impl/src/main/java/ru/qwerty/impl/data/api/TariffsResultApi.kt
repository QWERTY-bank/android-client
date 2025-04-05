package ru.qwerty.impl.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author y.yangel
 */
@Serializable
internal class TariffsResultApi(val results: List<TariffsApi>)

@Serializable
internal class TariffsApi(
    val id: String,
    val name: String,
    val interestRate: Double,
    val interestRateType: CreditTypeApi,
    val minPeriodDays: Int,
    val maxPeriodDays: Int,
)

@Serializable
internal enum class CreditTypeApi {

    @SerialName("Annual")
    Annual,

    @SerialName("Monthly")
    Monthly,

    @SerialName("Daytime")
    Daytime,
}