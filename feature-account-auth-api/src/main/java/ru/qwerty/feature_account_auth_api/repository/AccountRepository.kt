package ru.qwerty.feature_account_auth_api.repository

/**
 * @author y.yangel
 */
interface AccountRepository {
    suspend fun loginUserByPhone(phoneNumber: String, password: String)
}