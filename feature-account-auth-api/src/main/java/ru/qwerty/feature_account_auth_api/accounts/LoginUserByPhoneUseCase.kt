package ru.qwerty.feature_account_auth_api.accounts

import ru.qwerty.feature_account_auth_api.repository.AccountRepository

/**
 * @author y.yangel
 */
interface LoginUserByPhoneUseCase {
    suspend operator fun invoke(phoneNumber: String, password: String)
}

internal class LoginUserByPhoneUseCaseImpl(
    private val repository: AccountRepository,
) : LoginUserByPhoneUseCase {

    override suspend fun invoke(phoneNumber: String, password: String) {
        repository.loginUserByPhone(
            phoneNumber = phoneNumber,
            password = password,
        )
    }
}

class LoginUserByPhoneUseCaseFactory {
    fun create(repository: AccountRepository): LoginUserByPhoneUseCase {
        return LoginUserByPhoneUseCaseImpl(repository = repository)
    }
}