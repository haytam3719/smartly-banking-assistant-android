package com.ai.assistant.di

import com.ai.assistant.data.remote.BackendConfig
import com.ai.assistant.data.remote.BankingApi
import com.ai.assistant.data.repository.ChatRepository
import com.ai.assistant.data.repository.ChatRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides @Singleton
    fun provideBankingApi(client: OkHttpClient): BankingApi = Retrofit.Builder()
        .baseUrl(BackendConfig.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BankingApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindChatRepository(implementation: ChatRepositoryImpl): ChatRepository
}
