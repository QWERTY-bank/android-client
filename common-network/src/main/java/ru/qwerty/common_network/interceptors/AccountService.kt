package ru.qwerty.common_network.interceptors

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import ru.qwerty.common_network.common.RefreshTokenApi

/**
 * @author y.yangel
 */
interface AccountService {

    @POST("api/auth/login/code")
    suspend fun loginByCode(@Body loginApi: LoginApi): RefreshTokenApi

    @POST("api/auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") auth: String,
        @Body refreshApi: RefreshApi,
    ): RefreshTokenApi

    @GET("api/ClientUserSettings")
    suspend fun getHiddenAccounts(@Header("authorization") token: String): HiddenAccounts

    @POST("api/ClientUserSettings/accounts")
    suspend fun setHiddenAccount(
        @Query("showAccount") showAccount: Boolean,
        @Query("accountId") accountId: Int,
        @Header("authorization") token: String
    )
}

@Serializable
class HiddenAccounts(
    val id: String,
    val isNightTheme: Boolean,
    val hiddenAccounts: List<Int>,
)


@Serializable
class LoginApi(val code: String)

@Serializable
class RefreshApi(
    val refresh: String,
)