package ru.qwerty.common_network.interceptors

import android.util.Log
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.qwerty.common_network.common.CacheManager
import ru.qwerty.common_network.common.RefreshTokenApi
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject


/**
 * @author y.yangel
 */
internal class AuthInterceptor @Inject constructor(
    private val apiService: AccountService,
    private val sessionManager: SessionManager,
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = sessionManager.token

        if (accessToken != null && sessionManager.isAccessTokenExpired()) {
            val refreshToken = sessionManager.refreshToken

            if (refreshToken != null && !sessionManager.isRefreshTokenExpired()) {
                runBlocking {
                    runCatching {
                        apiService.refreshToken(
                            auth = "Bearer ${sessionManager.token}",
                            refreshApi = RefreshApi(refresh = refreshToken),
                        )
                    }.onFailure { error ->
                        Log.d("AuthInterceptor", "Error during refreshToken request", error)
                    }.onSuccess { refreshedTokens ->
                        sessionManager.updateToken(refreshedTokens)
                    }
                }
            }
        }

        val authorizedRequest = request.newBuilder()
            .header(AUTHORIZATION_HEADER, "Bearer ${sessionManager.token}")
            .build()

        return chain.proceed(authorizedRequest)
    }

    private companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
    }
}

class SessionManager @Inject constructor(private val cacheManager: CacheManager) {

    val token: String?
        get() = cacheManager.getToken()

    val refreshToken: String?
        get() = cacheManager.getRefreshToken()

    private val accessTokenExpiredAt: String?
        get() = cacheManager.getAccessTokenExpiredAt()

    private val refreshTokenExpiredAt: String?
        get() = cacheManager.getRefreshTokenExpiredAt()

    fun isAccessTokenExpired(): Boolean {
        val expiredAt = accessTokenExpiredAt ?: return true
        val instant = Instant.from(DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(expiredAt))
        return instant.toEpochMilli() < System.currentTimeMillis()
    }

    fun isRefreshTokenExpired(): Boolean {
        val expiredAt = refreshTokenExpiredAt ?: return true
        val instant = Instant.from(DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(expiredAt))
        return instant.toEpochMilli() < System.currentTimeMillis()
    }

    fun updateToken(tokenResponse: RefreshTokenApi) {
        cacheManager.saveResponse(tokenResponse)
    }
}