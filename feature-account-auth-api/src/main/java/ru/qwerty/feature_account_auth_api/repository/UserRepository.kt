package ru.qwerty.feature_account_auth_api.repository

/**
 * @author y.yangel
 */
interface UserRepository {

    suspend fun isUserAlreadyLogged(): Boolean

    suspend fun loginUser(code: String)

    suspend fun setHiddenAccount(isHidden: Boolean, accountId: Int)

    suspend fun getHiddenAccounts() : List<Int>
}