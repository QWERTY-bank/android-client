package ru.qwerty.feature.credits.api.usecases

import ru.qwerty.feature.credits.api.domain.Tariff
import ru.qwerty.feature.credits.api.repository.CreditsRepository

/**
 * @author y.yangel
 */
interface GetTariffUseCase {
    suspend operator fun invoke(): List<Tariff>
}

internal class GetTariffUseCaseImpl(private val repository: CreditsRepository) : GetTariffUseCase {

    override suspend operator fun invoke(): List<Tariff> {
        return repository.getCreditTariffs()
    }
}

class GetTariffUseCaseProvider(private val repository: CreditsRepository) {
    val useCase: GetTariffUseCase
        get() = GetTariffUseCaseImpl(repository = repository)
}