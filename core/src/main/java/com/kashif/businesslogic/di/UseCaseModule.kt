package com.kashif.businesslogic.di

import com.kashif.businesslogic.data.repository.IMovieRepository
import com.kashif.businesslogic.domain.usecase.MultiSearchMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideMultiSearchMoviesUseCase(movieRepository: IMovieRepository): MultiSearchMoviesUseCase {
        return MultiSearchMoviesUseCase(movieRepository)
    }
    @Provides
    @Singleton
    fun provideGetMovieDetailsUseCase(movieRepository: IMovieRepository): GetMovieDetailsUseCase {
        return GetMovieDetailsUseCase(movieRepository)
    }
}