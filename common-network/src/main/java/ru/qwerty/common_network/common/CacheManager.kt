package ru.qwerty.common_network.common

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.serialization.Serializable


/**
 * @author y.yangel
 */
@Serializable
class RefreshTokenApi(
    val refresh: String,
    val refreshExpiredDateTime: String,
    val access: String,
    val accessExpiredDateTime: String,
)

class CacheManager @Inject constructor(@ApplicationContext context: Context) {


    private companion object {
        private const val TOKEN = "token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val ACCESS_TOKEN_EXPIRED_AT = "access_token_expired_at"
        private const val REFRESH_TOKEN_EXPIRED_AT = "refresh_token_expired_at"
    }

    private val masterKey: MasterKey =
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

    private val encryptedSharedPreferences =
        EncryptedSharedPreferences.create(
            context, "my_secret_storage" , masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun saveResponse(tokenResponse: RefreshTokenApi) {
        saveToken(tokenResponse.access)
        saveRefreshToken(tokenResponse.refresh)
        saveAccessTokenExpiredAt(tokenResponse.accessExpiredDateTime)
        saveRefreshTokenExpiredAt(tokenResponse.refreshExpiredDateTime)
    }

    private fun saveToken(token: String) {
        encryptedSharedPreferences.edit().putString(TOKEN, token).apply()
    }

    fun getToken(): String? {
        return encryptedSharedPreferences.getString(TOKEN, null)
    }

    private fun saveRefreshToken(refreshToken: String) {
        encryptedSharedPreferences.edit().putString(REFRESH_TOKEN, refreshToken).apply()
    }

    fun getRefreshToken(): String? {
        return encryptedSharedPreferences.getString(REFRESH_TOKEN, null)
    }

    private fun saveAccessTokenExpiredAt(expiredAt: String) {
        encryptedSharedPreferences.edit().putString(ACCESS_TOKEN_EXPIRED_AT, expiredAt).apply()
    }

    fun getAccessTokenExpiredAt(): String? {
        return encryptedSharedPreferences.getString(ACCESS_TOKEN_EXPIRED_AT, null)
    }

    private fun saveRefreshTokenExpiredAt(expiredAt: String) {
        encryptedSharedPreferences.edit().putString(REFRESH_TOKEN_EXPIRED_AT, expiredAt).apply()
    }

    fun getRefreshTokenExpiredAt(): String? {
        return encryptedSharedPreferences.getString(REFRESH_TOKEN_EXPIRED_AT, null)
    }
}