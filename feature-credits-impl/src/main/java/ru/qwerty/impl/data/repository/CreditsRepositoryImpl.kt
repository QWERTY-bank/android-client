package ru.qwerty.impl.data.repository

import ru.qwerty.feature.credits.api.domain.Tariff
import ru.qwerty.feature.credits.api.repository.CreditsRepository
import ru.qwerty.impl.data.mapper.toDomain
import ru.qwerty.impl.services.CreditService
import javax.inject.Inject

/**
 * @author y.yangel
 */
internal class CreditsRepositoryImpl @Inject constructor(
    private val creditService: CreditService,
) : CreditsRepository {

    override suspend fun getCreditTariffs(): List<Tariff> {
        return creditService.getTariffs().results.map { it.toDomain() }
    }

}