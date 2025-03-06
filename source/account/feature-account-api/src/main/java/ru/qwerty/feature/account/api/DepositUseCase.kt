package ru.qwerty.feature.account.api

import ru.qwerty.feature.account.api.domain.Transaction
import ru.qwerty.feature.account.api.repository.AccountsRepository

/**
 * @author y.yangel
 */
interface DepositUseCase {
    suspend operator fun invoke(id: String, depositAmount: String)
}

internal class DepositUseCaseImpl(
    private val accountsRepository: AccountsRepository,
) : DepositUseCase {

    override suspend operator fun invoke(id: String, depositAmount: String) = accountsRepository.deposit(id, depositAmount)
}

class DepositUseCaseProvider(private val accountsRepository: AccountsRepository) {
    val useCase: DepositUseCase
        get() = DepositUseCaseImpl(accountsRepository = accountsRepository)
}