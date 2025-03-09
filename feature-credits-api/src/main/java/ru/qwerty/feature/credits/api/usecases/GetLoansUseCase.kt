package ru.qwerty.feature.credits.api.usecases

import ru.qwerty.feature.credits.api.domain.Loan
import ru.qwerty.feature.credits.api.domain.Tariff
import ru.qwerty.feature.credits.api.repository.CreditsRepository

/**
 * @author y.yangel
 */
interface GetLoansUseCase {
    suspend operator fun invoke(): List<Loan>
}

internal class GetLoansUseCaseImpl(private val repository: CreditsRepository) : GetLoansUseCase {

    override suspend operator fun invoke(): List<Loan> {
        return repository.getLoans()
    }
}

class GetLoansUseCaseProvider(private val repository: CreditsRepository) {
    val useCase: GetLoansUseCase
        get() = GetLoansUseCaseImpl(repository = repository)
}