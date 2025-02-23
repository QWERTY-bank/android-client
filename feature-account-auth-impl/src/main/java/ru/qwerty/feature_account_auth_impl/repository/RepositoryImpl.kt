package ru.qwerty.feature_account_auth_impl.repository

import jakarta.inject.Inject
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.qwerty.feature_account_auth_api.Cat
import ru.qwerty.feature_account_auth_api.Repository

/**
 * @author y.yangel
 */
internal class RepositoryImpl @Inject constructor(private val catService: CatService) : Repository {

    override suspend fun getCat(): Cat {
        return catService.getCats(10).map { it.toDomain() }.first()
    }

    private fun CatApi.toDomain(): Cat = Cat(url = url)
}

internal interface CatService {

    @GET("images/search")
    suspend fun getCats(@Query("limit") limit: Int): List<CatApi>
}

@Serializable
internal class CatApi(
    @SerialName("url")
    val url: String,
    @SerialName("id")
    val id: String,
)
