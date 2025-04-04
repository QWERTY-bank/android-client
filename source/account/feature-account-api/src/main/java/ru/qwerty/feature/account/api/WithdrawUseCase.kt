package ru.qwerty.feature.account.api

import ru.qwerty.feature.account.api.domain.Transaction
import ru.qwerty.feature.account.api.repository.AccountsRepository

/**
 * @author y.yangel
 */
interface WithdrawUseCase {
    suspend operator fun invoke(id: String, withdrawAmount: String, key: String, code: String)
}

internal class WithdrawUseCaseImpl(
    private val accountsRepository: AccountsRepository,
) : WithdrawUseCase {

    override suspend operator fun invoke(
        id: String,
        withdrawAmount: String,
        key: String,
        code: String
    ) {
        return accountsRepository.withdraw(id, withdrawAmount, key, code)
    }
}

class WithdrawUseCaseProvider(private val accountsRepository: AccountsRepository) {
    val useCase: WithdrawUseCase
        get() = WithdrawUseCaseImpl(accountsRepository = accountsRepository)
}