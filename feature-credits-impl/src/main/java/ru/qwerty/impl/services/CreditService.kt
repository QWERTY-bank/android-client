package ru.qwerty.impl.services

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.qwerty.impl.data.api.CreateLoanRequest
import ru.qwerty.impl.data.api.LoanApiResponse
import ru.qwerty.impl.data.api.LoanResponseApi
import ru.qwerty.impl.data.api.TariffsResultApi

/**
 * @author y.yangel
 */
internal interface CreditService {

    @GET("api/tariffs")
    suspend fun getTariffs(
        @Query("Page") page: Int = 1,
        @Query("Size") size: Int = 100,
    ): TariffsResultApi

    @GET("api/credits")
    suspend fun getLoans(
        @Query("Page") page: Int = 1,
        @Query("Size") size: Int = 100,
    ): LoanApiResponse

    @POST("api/credits/apply")
    suspend fun applyLoan(@Body createLoan: CreateLoanRequest)

    @GET("api/credits/{creditId}")
    suspend fun creditDetails(@Path("creditId") id: String): LoanResponseApi

    @POST("api/credits/{creditId}/reduce_payment")
    suspend fun reduceLoanPayment(
        @Path("creditId") id: String,
        @Body reduceLoanPayment: ReduceLoanPayment,
    )

    @GET("api/User")
    suspend fun getLoanRating() : Credit
}

@Serializable
internal class ReduceLoanPayment(
    val key: String,
    val accountId: Int,
    val value: Double,
)

@Serializable
data class Credit(
    val rating: Int,
    val maxRating: Int,
)
