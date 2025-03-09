package ru.qwerty.impl.data.mapper

import ru.qwerty.feature.credits.api.domain.CreditType
import ru.qwerty.feature.credits.api.domain.Tariff
import ru.qwerty.impl.data.api.CreditTypeApi
import ru.qwerty.impl.data.api.TariffsApi

/**
 * @author y.yangel
 */
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