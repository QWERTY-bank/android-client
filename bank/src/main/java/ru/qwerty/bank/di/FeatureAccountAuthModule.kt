package ru.qwerty.bank.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.qwerty.feature_account_auth_api.GetCatUseCase
import ru.qwerty.feature_account_auth_api.GetCatUseCaseFactory
import ru.qwerty.feature_account_auth_api.Repository

/**
 * @author y.yangel
 */
@Module
@InstallIn(ActivityComponent::class)
object FeatureAccountAuthModule {

    @Provides
    fun provideGetCatUseCase(repository: Repository): GetCatUseCase {
        return GetCatUseCaseFactory(repository = repository).getCatUseCase()
    }
}