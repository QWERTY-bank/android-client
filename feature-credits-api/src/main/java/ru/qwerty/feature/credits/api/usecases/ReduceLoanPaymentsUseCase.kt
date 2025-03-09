package ru.qwerty.feature.credits.api.usecases

import ru.qwerty.feature.credits.api.domain.LoanDetails
import ru.qwerty.feature.credits.api.repository.CreditsRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * @author y.yangel
 */

interface ReduceLoanPaymentsUseCase {
    suspend operator fun invoke(creditId: String, accountId: Int, value: Double)
}

@OptIn(ExperimentalUuidApi::class)
internal class ReduceLoanPaymentsUseCaseImpl(
    private val repository: CreditsRepository
) : ReduceLoanPaymentsUseCase {

    private val transactionKey by lazy { Uuid.random().toString() }

    override suspend operator fun invoke(creditId: String, accountId: Int, value: Double) {
        return repository.reduceLoanPayments(
            creditId = creditId,
            key = transactionKey,
            accountId = accountId,
            value = value
        )
    }
}

class ReduceLoanPaymentsUseCaseProvider(private val repository: CreditsRepository) {
    val useCase: ReduceLoanPaymentsUseCase
        get() = ReduceLoanPaymentsUseCaseImpl(repository = repository)
}