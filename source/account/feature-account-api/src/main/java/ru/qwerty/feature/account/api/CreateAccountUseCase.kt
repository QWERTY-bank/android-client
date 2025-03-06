package ru.qwerty.feature.account.api

import ru.qwerty.feature.account.api.repository.AccountsRepository

interface CreateAccountUseCase {
    suspend operator fun invoke(title: String)
}

internal class CreateAccountUseCaseImpl(
    private val accountsRepository: AccountsRepository,
) : CreateAccountUseCase {

    override suspend operator fun invoke(title: String) = accountsRepository.createAccount(title)
}

class CreateAccountUseCaseProvider(private val accountsRepository: AccountsRepository) {
    val useCase: CreateAccountUseCase
        get() = CreateAccountUseCaseImpl(accountsRepository = accountsRepository)
}