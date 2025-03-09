package ru.qwerty.bank.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.qwerty.feature.credits.api.repository.CreditsRepository
import ru.qwerty.feature.credits.api.usecases.ApplyLoansUseCase
import ru.qwerty.feature.credits.api.usecases.ApplyLoansUseCaseProvider
import ru.qwerty.feature.credits.api.usecases.GetLoanDetailsUseCase
import ru.qwerty.feature.credits.api.usecases.GetLoanDetailsUseCaseProvider
import ru.qwerty.feature.credits.api.usecases.GetLoansUseCase
import ru.qwerty.feature.credits.api.usecases.GetLoansUseCaseProvider
import ru.qwerty.feature.credits.api.usecases.GetTariffUseCase
import ru.qwerty.feature.credits.api.usecases.GetTariffUseCaseProvider
import ru.qwerty.feature.credits.api.usecases.ReduceLoanPaymentsUseCase
import ru.qwerty.feature.credits.api.usecases.ReduceLoanPaymentsUseCaseProvider

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

    @Provides
    fun provideGetLoansUseCase(repository: CreditsRepository): GetLoansUseCase {
        return GetLoansUseCaseProvider(repository = repository).useCase
    }

    @Provides
    fun provideLoansApplyUseCase(repository: CreditsRepository): ApplyLoansUseCase {
        return ApplyLoansUseCaseProvider(repository = repository).useCase
    }

    @Provides
    fun provideGetLoanDetailsUseCase(repository: CreditsRepository): GetLoanDetailsUseCase {
        return GetLoanDetailsUseCaseProvider(repository = repository).useCase
    }

    @Provides
    fun provideReduceLoanPaymentsUseCase(repository: CreditsRepository): ReduceLoanPaymentsUseCase {
        return ReduceLoanPaymentsUseCaseProvider(repository = repository).useCase
    }
}
