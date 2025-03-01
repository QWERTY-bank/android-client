package ru.qwerty.common_network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import ru.qwerty.common_network.BuildConfig
import ru.qwerty.common_network.interceptors.AccountService
import ru.qwerty.common_network.interceptors.AuthInterceptor
import javax.inject.Named
import javax.inject.Singleton

/**
 * @author y.yangel
 */
@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    @Named("Account")
    fun provideRetrofit(authInterceptor: AuthInterceptor): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_ACCOUNT_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .run { client(okHttpClient) }
            .build()
    }

    @Provides
    internal fun provideAccountService(@Named("Account") retrofit: Retrofit): AccountService {
        return retrofit.create(AccountService::class.java)
    }
}
