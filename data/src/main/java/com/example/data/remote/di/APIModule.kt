package com.example.data.remote.di

import com.example.data.remote.TestDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Singleton
    @Provides
    fun provideTestAPI(retrofit: Retrofit): TestDataSource =
        retrofit.create(TestDataSource::class.java)
}