package ru.qwerty.bank.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.qwerty.feature.account.api.CreateAccountUseCase
import ru.qwerty.feature.account.api.CreateAccountUseCaseProvider
import ru.qwerty.feature.account.api.DeleteAccountUseCase
import ru.qwerty.feature.account.api.DeleteAccountUseCaseProvider
import ru.qwerty.feature.account.api.DepositUseCase
import ru.qwerty.feature.account.api.DepositUseCaseProvider
import ru.qwerty.feature.account.api.GetAccountBalanceUseCase
import ru.qwerty.feature.account.api.GetAccountBalanceUseCaseProvider
import ru.qwerty.feature.account.api.GetTransactionsByAccountIdUseCase
import ru.qwerty.feature.account.api.GetTransactionsByAccountIdUseCaseProvider
import ru.qwerty.feature.account.api.GetUserAccountUseCase
import ru.qwerty.feature.account.api.GetUserAccountUseCaseImpl
import ru.qwerty.feature.account.api.ObtainTransactionsUseCase
import ru.qwerty.feature_account_auth_api.IsUserAlreadyLoginUseCase
import ru.qwerty.feature_account_auth_api.IsUserAlreadyLoginUseCaseProvider
import ru.qwerty.feature.account.api.WithdrawUseCase
import ru.qwerty.feature.account.api.WithdrawUseCaseProvider
import ru.qwerty.feature.account.api.repository.AccountsRepository
import ru.qwerty.feature.account.api.repository.TransactionRepository
import ru.qwerty.feature_account_auth_api.repository.UserRepository

/**
 * @author y.yangel
 */
@Module
@InstallIn(ViewModelComponent::class)
object FeatureAccountModule {

    @Provides
    fun provideGetAccountsUseCase(accountsRepository: AccountsRepository): GetUserAccountUseCase {
        return GetUserAccountUseCaseImpl(accountsRepository)
    }

    @Provides
    fun provideCreateAccountUseCase(accountsRepository: AccountsRepository): CreateAccountUseCase {
        return CreateAccountUseCaseProvider(accountsRepository).useCase
    }

    @Provides
    fun provideGetTransactionsUseCase(accountsRepository: AccountsRepository): GetTransactionsByAccountIdUseCase {
        return GetTransactionsByAccountIdUseCaseProvider(accountsRepository).useCase
    }

    @Provides
    fun provideDepositUseCase(accountsRepository: AccountsRepository): DepositUseCase {
        return DepositUseCaseProvider(accountsRepository).useCase
    }

    @Provides
    fun provideWithdrawUseCase(accountsRepository: AccountsRepository): WithdrawUseCase {
        return WithdrawUseCaseProvider(accountsRepository).useCase
    }

    @Provides
    fun provideDeleteAccountUseCase(accountsRepository: AccountsRepository): DeleteAccountUseCase {
        return DeleteAccountUseCaseProvider(accountsRepository).useCase
    }

    @Provides
    fun provideIsUserAlreadyLoginUseCase(userRepository: UserRepository): IsUserAlreadyLoginUseCase {
        return IsUserAlreadyLoginUseCaseProvider(userRepository).useCase
    }

    @Provides
    fun provideGetAccountBalanceUseCase(accountsRepository: AccountsRepository): GetAccountBalanceUseCase {
        return GetAccountBalanceUseCaseProvider(accountsRepository).useCase
    }

    @Provides
    fun provideObtainTransactionsUseCase(transactionRepository: TransactionRepository) : ObtainTransactionsUseCase {
        return ObtainTransactionsUseCase(transactionRepository)
    }
}
