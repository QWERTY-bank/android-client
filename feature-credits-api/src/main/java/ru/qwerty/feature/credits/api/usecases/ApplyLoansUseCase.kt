package ru.qwerty.feature.credits.api.usecases

import ru.qwerty.feature.credits.api.repository.CreditsRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * @author y.yangel
 */
interface ApplyLoansUseCase {
    suspend operator fun invoke(
        accountId: Int,
        tariffId: String,
        periodDays: Int,
        loanAmount: Double,
    )
}

@OptIn(ExperimentalUuidApi::class)
internal class ApplyLoansUseCaseImpl(private val repository: CreditsRepository) : ApplyLoansUseCase {

    private val transactionKey by lazy { Uuid.random().toString() }

    override suspend operator fun invoke(
        accountId: Int,
        tariffId: String,
        periodDays: Int,
        loanAmount: Double,
    ) {
        return repository.applyLoan(
            key = transactionKey,
            accountId = accountId,
            tariffId = tariffId,
            periodDays = periodDays,
            loanAmount = loanAmount,
        )
    }
}

class ApplyLoansUseCaseProvider(private val repository: CreditsRepository) {
    val useCase: ApplyLoansUseCase
        get() = ApplyLoansUseCaseImpl(repository = repository)
}