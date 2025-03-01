package ru.qwerty.feature_account_auth_impl.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.qwerty.common_network.interceptors.AccountService
import ru.qwerty.feature_account_auth_api.repository.AccountRepository
import ru.qwerty.feature_account_auth_impl.data.repository.AccountRepositoryImpl

/**
 * @author y.yangel
 */
@Module
@InstallIn(SingletonComponent::class)
class FeatureAccountAuthImplModule {

    @Provides
    internal fun provideRepository(accountService: AccountService) : AccountRepository {
        return AccountRepositoryImpl(accountService = accountService)
    }
}