package ru.qwerty.feature_account_auth_impl.data.repository

import ru.qwerty.feature_account_auth_api.repository.AccountRepository
import ru.qwerty.feature_account_auth_impl.BuildConfig
import ru.qwerty.common_network.interceptors.AccountService
import ru.qwerty.common_network.interceptors.LoginApi

/**
 * @author y.yangel
 */
internal class AccountRepositoryImpl(
    private val accountService: AccountService,
) : AccountRepository {

    override suspend fun loginUserByPhone(phoneNumber: String, password: String) {
        accountService.loginByPhoneNumber(
            loginApi = createLoginApi(phoneNumber, password)
        )
    }

    private fun createLoginApi(phoneNumber: String, password: String): LoginApi {
        return LoginApi(
            phone = "+$phoneNumber",
            password = password,
        )
    }
}