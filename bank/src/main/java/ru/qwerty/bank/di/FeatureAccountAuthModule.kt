package ru.qwerty.bank.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.qwerty.feature_account_auth_api.accounts.LoginUserUseCase
import ru.qwerty.feature_account_auth_api.accounts.LoginUserByPhoneUseCaseFactory
import ru.qwerty.feature_account_auth_api.repository.UserRepository

/**
 * @author y.yangel
 */
@Module
@InstallIn(ViewModelComponent::class)
object FeatureAccountAuthModule {

    @Provides
    fun provideLoginUserByPhoneUseCase(repository: UserRepository): LoginUserUseCase {
        return LoginUserByPhoneUseCaseFactory().create(repository = repository)
    }


}