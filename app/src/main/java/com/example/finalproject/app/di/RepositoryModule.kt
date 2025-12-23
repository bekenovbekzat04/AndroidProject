package com.example.finalproject.app.di

import com.example.finalproject.core.data.repository.AnimeRepositoryImpl
import com.example.finalproject.core.domain.AnimeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAnimeRepository(
        impl: AnimeRepositoryImpl
    ): AnimeRepository = impl
}
