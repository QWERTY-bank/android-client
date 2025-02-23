package ru.qwerty.feature_account_auth_impl.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.qwerty.feature_account_auth_api.Repository
import ru.qwerty.feature_account_auth_impl.repository.CatService
import ru.qwerty.feature_account_auth_impl.repository.RepositoryImpl


/**
 * @author y.yangel
 */
@Module
@InstallIn(SingletonComponent::class)
class FeatureAccountAuthImplModule {

    @Provides
    internal fun provideCatService(retrofit: Retrofit) : CatService {
        return retrofit.create(CatService::class.java)
    }

    @Provides
    internal fun provideRepository(catService: CatService) : Repository {
        return RepositoryImpl(catService = catService)
    }
}