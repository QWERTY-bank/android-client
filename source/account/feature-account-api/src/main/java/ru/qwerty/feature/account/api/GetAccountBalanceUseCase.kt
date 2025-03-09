package ru.qwerty.feature.account.api

import ru.qwerty.feature.account.api.repository.AccountsRepository

/**
 * @author y.yangel
 */

interface GetAccountBalanceUseCase {
    suspend operator fun invoke(accountId: String) : Double
}

internal class GetAccountBalanceUseCaseImpl(
    private val accountsRepository: AccountsRepository,
) : GetAccountBalanceUseCase {

    override suspend operator fun invoke(accountId: String): Double {
        return accountsRepository.getAccountBalance(accountId)
    }
}

class GetAccountBalanceUseCaseProvider(private val accountsRepository: AccountsRepository) {
    val useCase: GetAccountBalanceUseCase
        get() = GetAccountBalanceUseCaseImpl(accountsRepository = accountsRepository)
}