package com.nuzhnov.workcontrol.core.data.api.di

import com.nuzhnov.workcontrol.core.data.api.authenticator.ServiceAuthenticator
import com.nuzhnov.workcontrol.core.data.api.constant.BACKEND_ENDPOINT
import com.squareup.moshi.Moshi
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient

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
