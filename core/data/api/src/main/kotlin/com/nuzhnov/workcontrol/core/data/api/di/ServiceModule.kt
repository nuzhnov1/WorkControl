package com.nuzhnov.workcontrol.core.data.api.di

import com.nuzhnov.workcontrol.core.data.api.service.*
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@[Module InstallIn(SingletonComponent::class)]
internal object ServiceModule {
    @[Provides Singleton]
    fun provideAuthorizationService(retrofit: Retrofit): AuthorizationService =
        retrofit.create(AuthorizationService::class.java)

    @[Provides Singleton]
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @[Provides Singleton]
    fun provideUniversityService(retrofit: Retrofit): UniversityService =
        retrofit.create(UniversityService::class.java)

    @[Provides Singleton]
    fun provideLessonService(retrofit: Retrofit): LessonService =
        retrofit.create(LessonService::class.java)

    @[Provides Singleton]
    fun provideStatisticsService(retrofit: Retrofit): StatisticsService =
        retrofit.create(StatisticsService::class.java)
}
