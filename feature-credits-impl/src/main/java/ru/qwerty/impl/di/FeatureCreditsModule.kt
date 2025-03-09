package ru.qwerty.impl.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.qwerty.feature.credits.api.repository.CreditsRepository
import ru.qwerty.impl.data.repository.CreditsRepositoryImpl
import ru.qwerty.impl.services.CreditService
import javax.inject.Named

/**
 * @author y.yangel
 */
@Module
@InstallIn(SingletonComponent::class)
internal object FeatureCreditsModule {

    @Provides
    fun provideAccountsService(@Named("Credit") retrofit: Retrofit): CreditService {
        return retrofit.create(CreditService::class.java)
    }

    @Provides
    fun provideCreditsRepository(creditService: CreditService): CreditsRepository {
        return CreditsRepositoryImpl(creditService = creditService)
    }
}