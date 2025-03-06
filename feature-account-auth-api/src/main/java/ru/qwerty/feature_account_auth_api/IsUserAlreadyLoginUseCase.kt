package ru.qwerty.feature_account_auth_api

import ru.qwerty.feature_account_auth_api.repository.AccountRepository

/**
 * @author y.yangel
 */
interface IsUserAlreadyLoginUseCase {
    suspend operator fun invoke(): Boolean
}

internal class IsUserAlreadyLoginUseCaseImpl(
    private val accountsRepository: AccountRepository,
) : IsUserAlreadyLoginUseCase {

    override suspend operator fun invoke(): Boolean = accountsRepository.isUserAlreadyLogged()

}

class IsUserAlreadyLoginUseCaseProvider(private val accountsRepository: AccountRepository) {
    val useCase: IsUserAlreadyLoginUseCase
        get() = IsUserAlreadyLoginUseCaseImpl(accountsRepository = accountsRepository)
}