package ru.qwerty.bank.di

import android.accounts.Account
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import ru.qwerty.feature_account_auth_api.GetCatUseCase
import ru.qwerty.feature_account_auth_api.GetCatUseCaseFactory
import ru.qwerty.feature_account_auth_api.Repository
import ru.qwerty.feature_account_auth_api.accounts.LoginUserByPhoneUseCase
import ru.qwerty.feature_account_auth_api.accounts.LoginUserByPhoneUseCaseFactory
import ru.qwerty.feature_account_auth_api.repository.AccountRepository

/**
 * @author y.yangel
 */
@Module
@InstallIn(ViewModelComponent::class)
object FeatureAccountAuthModule {

    @Provides
    fun provideLoginUserByPhoneUseCase(repository: AccountRepository): LoginUserByPhoneUseCase {
        return LoginUserByPhoneUseCaseFactory().create(repository = repository)
    }
}