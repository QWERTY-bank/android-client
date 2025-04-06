package ru.qwerty.feature_account_auth_api.accounts

import ru.qwerty.feature_account_auth_api.repository.UserRepository

/**
 * @author y.yangel
 */
interface LoginUserUseCase {
    suspend operator fun invoke(code: String)
}

internal class LoginUserUseCaseImpl(
    private val repository: UserRepository,
) : LoginUserUseCase {

    override suspend fun invoke(code: String) {
        repository.loginUser(code)
    }
}

class LoginUserByPhoneUseCaseFactory {
    fun create(repository: UserRepository): LoginUserUseCase {
        return LoginUserUseCaseImpl(repository = repository)
    }
}