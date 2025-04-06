package ru.qwerty.feature_account_auth_impl.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.qwerty.common_network.common.CacheManager
import ru.qwerty.common_network.interceptors.AccountService
import ru.qwerty.feature_account_auth_api.repository.UserRepository
import ru.qwerty.feature_account_auth_impl.data.repository.UserRepositoryImpl

/**
 * @author y.yangel
 */
@Module
@InstallIn(SingletonComponent::class)
class FeatureAccountAuthImplModule {

    @Provides
    internal fun provideRepository(
        accountService: AccountService,
        cacheManager: CacheManager,
    ): UserRepository {
        return UserRepositoryImpl(accountService = accountService, cacheManager = cacheManager)
    }
}