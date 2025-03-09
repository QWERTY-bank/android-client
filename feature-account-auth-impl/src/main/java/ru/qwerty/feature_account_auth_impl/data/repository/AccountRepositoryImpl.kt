package ru.qwerty.feature_account_auth_impl.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.qwerty.common_network.common.CacheManager
import ru.qwerty.feature_account_auth_api.repository.AccountRepository
import ru.qwerty.common_network.interceptors.AccountService
import ru.qwerty.common_network.interceptors.LoginApi

/**
 * @author y.yangel
 */
internal class AccountRepositoryImpl(
    private val accountService: AccountService,
    private val cacheManager: CacheManager,
) : AccountRepository {

    override suspend fun isUserAlreadyLogged(): Boolean {
        return cacheManager.getToken() != null
    }

    override suspend fun loginUserByPhone(phoneNumber: String, password: String) {
        val refreshTokenApi = accountService.loginByPhoneNumber(
            loginApi = createLoginApi(phoneNumber, password)
        )
        cacheManager.saveResponse(refreshTokenApi)
    }

    private fun createLoginApi(phoneNumber: String, password: String): LoginApi {
        return LoginApi(
            phone = "+$phoneNumber",
            password = password,
        )
    }
}