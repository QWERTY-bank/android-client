package ru.qwerty.feature.credits.ui.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * @author y.yangel
 */
@Serializable
internal class ApplyLoansDirection(
    val tariffId: String,
    val minPeriodDays: Int,
    val maxPeriodDays: Int,
)