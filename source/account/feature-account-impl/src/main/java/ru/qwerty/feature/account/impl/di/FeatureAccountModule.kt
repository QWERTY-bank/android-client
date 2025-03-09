package ru.qwerty.feature.account.impl.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.qwerty.common_network.common.CacheManager
import ru.qwerty.feature.account.api.repository.AccountsRepository
import ru.qwerty.feature.account.impl.repository.AccountsRepositoryImpl
import ru.qwerty.feature.account.impl.services.AccountsService
import javax.inject.Named

/**
 * @author y.yangel
 */
@Module
@InstallIn(SingletonComponent::class)
internal object FeatureAccountModule {

    @Provides
    fun provideAccountsService(@Named("Core") retrofit: Retrofit): AccountsService {
        return retrofit.create(AccountsService::class.java)
    }

    @Provides
    fun provideAccountsRepository(service: AccountsService, cacheManager: CacheManager) : AccountsRepository {
        return AccountsRepositoryImpl(accountsService = service, cacheManager = cacheManager)
    }
}