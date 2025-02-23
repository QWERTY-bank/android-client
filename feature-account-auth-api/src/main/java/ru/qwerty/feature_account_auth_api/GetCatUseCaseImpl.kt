package ru.qwerty.feature_account_auth_api


internal class GetCatUseCaseImpl(private val repository: Repository) : GetCatUseCase {

    override suspend fun invoke(): Cat {
        return repository.getCat()
    }
}

interface GetCatUseCase {
    suspend operator fun invoke(): Cat
}

interface Repository {
    suspend fun getCat(): Cat
}

@JvmInline
value class Cat(val url: String?)


class GetCatUseCaseFactory(private val repository: Repository) {

    fun getCatUseCase(): GetCatUseCase {
        return GetCatUseCaseImpl(repository)
    }
}
