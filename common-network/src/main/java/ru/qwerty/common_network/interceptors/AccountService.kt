package ru.qwerty.common_network.interceptors

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST
import ru.qwerty.common_network.common.RefreshTokenApi

/**
 * @author y.yangel
 */
interface AccountService {

    @POST("api/auth/login")
    suspend fun loginByPhoneNumber(@Body loginApi: LoginApi) : RefreshTokenApi

    @POST("api/auth/refresh")
    suspend fun refreshToken(@Body refreshApi: RefreshApi) : RefreshTokenApi

}

@Serializable
class LoginApi(val phone: String, val password: String)

@Serializable
class RefreshApi(
    val refresh: String,
)