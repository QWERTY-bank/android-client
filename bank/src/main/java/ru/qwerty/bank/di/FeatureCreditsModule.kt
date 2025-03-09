package ru.qwerty.bank.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.qwerty.feature.credits.api.repository.CreditsRepository
import ru.qwerty.feature.credits.api.usecases.GetTariffUseCase
import ru.qwerty.feature.credits.api.usecases.GetTariffUseCaseProvider

/**
 * @author y.yangel
 */
@Module
@InstallIn(ViewModelComponent::class)
object FeatureCreditsModule {

    @Provides
    fun provideGetTariffsUseCase(repository: CreditsRepository): GetTariffUseCase {
        return GetTariffUseCaseProvider(repository = repository).useCase
    }

}