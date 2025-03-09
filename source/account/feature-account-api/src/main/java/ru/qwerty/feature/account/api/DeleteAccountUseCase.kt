package ru.qwerty.feature.account.api

import ru.qwerty.feature.account.api.repository.AccountsRepository

/**
 * @author y.yangel
 */
interface DeleteAccountUseCase {
    suspend operator fun invoke(id: String)
}

internal class DeleteAccountUseCaseImpl(
    private val accountsRepository: AccountsRepository,
) : DeleteAccountUseCase {

    override suspend operator fun invoke(id: String) = accountsRepository.deleteAccount(id)
}

class DeleteAccountUseCaseProvider(private val accountsRepository: AccountsRepository) {
    val useCase: DeleteAccountUseCase
        get() = DeleteAccountUseCaseImpl(accountsRepository = accountsRepository)
}