package ru.qwerty.feature.credits.ui.mappers

import ru.qwerty.feature.credits.api.domain.Tariff
import ru.qwerty.feature.credits.ui.presentation.composable.tariff.viewmodel.TariffUi

/**
 * @author y.yangel
 */
internal fun Tariff.toUi(): TariffUi {
    return TariffUi(
        id = id,
        name = name,
        interestRate = interestRate,
        interestRateType = interestRateType,
        minPeriodDays = minPeriodDays,
        maxPeriodDays = maxPeriodDays,
    )
}