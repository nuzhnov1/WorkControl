package com.nuzhnov.workcontrol.core.api.di

import com.nuzhnov.workcontrol.core.api.authenticator.ServiceAuthenticator
import com.nuzhnov.workcontrol.core.api.constant.BACKEND_ENDPOINT
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@[Module InstallIn(SingletonComponent::class)]
internal object NetworkModule {
    @[Provides Singleton]
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BACKEND_ENDPOINT)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()

    @[Provides Singleton]
    fun provideOkHttpClient(serviceAuthenticator: ServiceAuthenticator): OkHttpClient =
        OkHttpClient.Builder()
            .authenticator(serviceAuthenticator)
            .build()

    @[Provides Singleton]
    fun provideMoshi(): Moshi = Moshi.Builder().build()
}