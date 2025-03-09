package ru.qwerty.impl.services

import retrofit2.http.GET
import retrofit2.http.Query
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

}
