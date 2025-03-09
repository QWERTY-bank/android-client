package ru.qwerty.feature.credits.api.usecases

import ru.qwerty.feature.credits.api.domain.LoanDetails
import ru.qwerty.feature.credits.api.repository.CreditsRepository

/**
 * @author y.yangel
 */
interface GetLoanDetailsUseCase {
    suspend operator fun invoke(id: String): LoanDetails
}

internal class GetLoanDetailsUseCaseImpl(private val repository: CreditsRepository) :
    GetLoanDetailsUseCase {

    override suspend operator fun invoke(id: String): LoanDetails {
        return repository.getLoanDetails(id)
    }
}

class GetLoanDetailsUseCaseProvider(private val repository: CreditsRepository) {
    val useCase: GetLoanDetailsUseCase
        get() = GetLoanDetailsUseCaseImpl(repository = repository)
}