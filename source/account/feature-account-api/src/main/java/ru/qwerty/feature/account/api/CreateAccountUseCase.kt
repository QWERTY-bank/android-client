package ru.qwerty.feature.account.api

import ru.qwerty.feature.account.api.repository.AccountsRepository

fun interface CreateAccountUseCase {
    suspend operator fun invoke(title: String, amountType: String)
}

internal class CreateAccountUseCaseImpl(
    private val accountsRepository: AccountsRepository,
) : CreateAccountUseCase {

    override suspend operator fun invoke(title: String, amountType: String) {
        return accountsRepository.createAccount(title, amountType)
    }
}

class CreateAccountUseCaseProvider(private val accountsRepository: AccountsRepository) {
    val useCase: CreateAccountUseCase
        get() = CreateAccountUseCaseImpl(accountsRepository = accountsRepository)
}