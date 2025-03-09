package ru.qwerty.feature.credits.api.repository

import ru.qwerty.feature.credits.api.domain.Tariff

/**
 * @author y.yangel
 */
interface CreditsRepository {
    suspend fun getCreditTariffs(): List<Tariff>
}