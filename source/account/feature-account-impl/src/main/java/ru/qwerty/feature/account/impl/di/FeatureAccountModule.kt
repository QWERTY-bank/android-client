package ru.qwerty.feature.account.impl.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.qwerty.common_network.common.CacheManager
import ru.qwerty.feature.account.api.repository.AccountsRepository
import ru.qwerty.feature.account.api.repository.TransactionRepository
import ru.qwerty.feature.account.impl.repository.AccountsRepositoryImpl
import ru.qwerty.feature.account.impl.repository.TransactionRepositoryImpl
import ru.qwerty.feature.account.impl.services.AccountsService
import ru.qwerty.feature.account.impl.services.api.transaction.TransactionWebSocketService
import ru.qwerty.feature.account.impl.services.api.transaction.TransactionWebSocketServiceImpl
import javax.inject.Named
import javax.inject.Singleton

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
    fun provideAccountsRepository(
        service: AccountsService,
        cacheManager: CacheManager
    ): AccountsRepository {
        return AccountsRepositoryImpl(accountsService = service, cacheManager = cacheManager)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(transactionWebSocketService: TransactionWebSocketService): TransactionRepository {
        return TransactionRepositoryImpl(transactionWebSocketService = transactionWebSocketService)
    }

    @Provides
    @Singleton
    fun provideTransactionWebSocketService(cacheManager: CacheManager): TransactionWebSocketService {
        return TransactionWebSocketServiceImpl(cacheManager = cacheManager)
    }
}