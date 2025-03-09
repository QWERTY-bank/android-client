package ru.qwerty.feature.account.api

import ru.qwerty.feature.account.api.domain.AccountTransactions
import ru.qwerty.feature.account.api.repository.AccountsRepository

/**
 * @author y.yangel
 */
interface GetTransactionsByAccountIdUseCase {
    suspend operator fun invoke(id: String): AccountTransactions
}

internal class GetTransactionsByAccountIdUseCaseImpl(
    private val accountsRepository: AccountsRepository,
) : GetTransactionsByAccountIdUseCase {

    override suspend operator fun invoke(id: String): AccountTransactions =
        accountsRepository.getTransactions(id)
}

class GetTransactionsByAccountIdUseCaseProvider(private val accountsRepository: AccountsRepository) {
    val useCase: GetTransactionsByAccountIdUseCase
        get() = GetTransactionsByAccountIdUseCaseImpl(accountsRepository = accountsRepository)
}