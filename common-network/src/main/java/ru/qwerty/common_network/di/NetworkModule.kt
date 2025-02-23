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
import javax.inject.Singleton

/**
 * @author y.yangel
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .run { client(okHttpClient) }
            .build()
    }

}

