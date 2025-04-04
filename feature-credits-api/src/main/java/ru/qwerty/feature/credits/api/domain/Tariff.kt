package ru.qwerty.feature.credits.api.domain

/**
 * @author y.yangel
 */
class Tariff(
    val id: String,
    val name: String,
    val interestRate: Double,
    val interestRateType: CreditType,
    val minPeriodDays: Int,
    val maxPeriodDays: Int,
)

enum class CreditType {
    Annual,
    Monthly,
    Daytime,
}