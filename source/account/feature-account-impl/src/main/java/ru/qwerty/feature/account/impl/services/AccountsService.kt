package ru.qwerty.feature.account.impl.services

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.qwerty.feature.account.impl.services.api.AccountApi
import ru.qwerty.feature.account.impl.services.api.transaction.AccountTransactionApi
import ru.qwerty.feature.account.impl.services.api.transaction.AccountBalanceRequest
import ru.qwerty.feature.account.impl.services.api.transaction.TransactionApi
import ru.qwerty.feature.account.impl.services.api.transaction.TransactionRequest

/**
 * @author y.yangel
 */
internal interface AccountsService {

    @GET("accounts")
    suspend fun getAccounts(): List<AccountApi>

    @POST("accounts")
    suspend fun createAccount(@Body createAccount: CreateAccount)

    @GET("accounts/{id}/balance")
    suspend fun getAccountBalance(@Path("id") id: Int): AccountBalanceRequest

    @GET("accounts/{id}/transactions")
    suspend fun getTransaction(@Path("id") id: Int): AccountTransactionApi

    @POST("accounts/{id}/deposit")
    suspend fun deposit(@Path("id") id: Int, @Body transactionRequest: TransactionRequest)

    @POST("accounts/{id}/withdraw")
    suspend fun withdraw(@Path("id") id: Int, @Body transactionRequest: TransactionRequest)

    @DELETE("accounts/{id}")
    suspend fun deleteAccount(@Path("id") id: Int)

}

@Serializable
internal class CreateAccount(
    @SerialName("title")
    val title: String,
    @SerialName("code")
    val code: String,
)