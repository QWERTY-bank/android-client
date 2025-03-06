package ru.qwerty.common_network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.qwerty.common_network.BuildConfig
import ru.qwerty.common_network.interceptors.AccountService
import ru.qwerty.common_network.interceptors.AuthInterceptor
import java.util.concurrent.TimeUnit
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
    fun provideRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_ACCOUNT_URL)
            .addConverterFactory(json.asConverterFactory("application/json; charset=utf-8".toMediaType()))
            .run { client(client) }
            .build()
    }

    @Provides
    @Singleton
    @Named("Core")
    fun provideCoreRetrofit(authInterceptor: AuthInterceptor): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_CORE_URL)
            .addConverterFactory(json.asConverterFactory("application/json; charset=utf-8".toMediaType()))
            .run { client(okHttpClient) }
            .build()
    }

    @Provides
    internal fun provideAccountService(@Named("Account") retrofit: Retrofit): AccountService {
        return retrofit.create(AccountService::class.java)
    }
}
