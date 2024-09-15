package com.kashif.businesslogic.di

import com.kashif.businesslogic.data.remote.service.MovieDbService
import com.kashif.businesslogic.data.repository.IMovieRepository
import com.kashif.businesslogic.data.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {


    @Provides
    @Singleton
    fun providesMovieService(retrofit: Retrofit): MovieDbService{
        return retrofit.create(MovieDbService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(movieDbService: MovieDbService): MovieRepositoryImpl {
        return MovieRepositoryImpl(movieDbService)
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal interface MoviesRepositoryModule {
    @Binds
    fun bindsMovieRepository(
        movieRepository: MovieRepositoryImpl,
    ): IMovieRepository
}