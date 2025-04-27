package ru.qwerty.feature_account_auth_api.repository

/**
 * @author y.yangel
 */
interface FirebaseRepository {

    suspend fun obtainFirebaseToken()
}