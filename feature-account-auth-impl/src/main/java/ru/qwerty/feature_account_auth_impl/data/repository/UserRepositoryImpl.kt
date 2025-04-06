package ru.qwerty.feature_account_auth_impl.data.repository

import ru.qwerty.common_network.common.CacheManager
import ru.qwerty.feature_account_auth_api.repository.UserRepository
import ru.qwerty.common_network.interceptors.AccountService
import ru.qwerty.common_network.interceptors.LoginApi

/**
 * @author y.yangel
 */
internal class UserRepositoryImpl(
    private val accountService: AccountService,
    private val cacheManager: CacheManager,
) : UserRepository {

    override suspend fun isUserAlreadyLogged(): Boolean {
        return cacheManager.getToken() != null
    }

    override suspend fun loginUser(code: String) {
        val refreshTokenApi = accountService.loginByCode(
            loginApi = createLoginApi(code)
        )
        cacheManager.saveResponse(refreshTokenApi)
    }

    override suspend fun setHiddenAccount(isHidden: Boolean, accountId: Int) {
        val token = requireNotNull(cacheManager.getToken()) { "cacheManager.getToken() is null" }
        accountService.setHiddenAccount(
            showAccount = isHidden,
            accountId = accountId,
            token = "Bearer $token"
        )
    }

    override suspend fun getHiddenAccounts(): List<Int> {
        val token = requireNotNull(cacheManager.getToken()) { "cacheManager.getToken() is null" }
        return accountService.getHiddenAccounts(
            token = "Bearer $token"
        ).hiddenAccounts
    }

    private fun createLoginApi(code: String): LoginApi {
        return LoginApi(code)
    }
}