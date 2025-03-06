package ru.qwerty.feature.account.api

import ru.qwerty.feature.account.api.domain.Account
import ru.qwerty.feature.account.api.repository.AccountsRepository

interface GetUserAccountUseCase {
    suspend operator fun invoke(): List<Account>
}

class GetUserAccountUseCaseImpl(
    private val accountsRepository: AccountsRepository,
) : GetUserAccountUseCase {

    override suspend operator fun invoke(): List<Account> {
        val accounts = accountsRepository.getAccounts()
        return accounts
    }
}

class GetUserAccountUseCaseProvider(private val accountsRepository: AccountsRepository) {
    val useCase: GetUserAccountUseCase
        get() = GetUserAccountUseCaseImpl(accountsRepository = accountsRepository)
}
