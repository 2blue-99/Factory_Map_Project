package com.example.data.remote.di

import com.example.data.remote.TestDataSource
import com.example.data.remote.TestDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideDataSource(dataSource: TestDataSource): TestDataSourceImpl = TestDataSourceImpl(dataSource)
}