package com.example.data.remote.di

import com.example.data.remote.datasource.AllAreaDataSource
import com.example.data.remote.datasource.AllAreaDataSourceImpl
import com.example.data.remote.datasource.GyeonggiDataSource
import com.example.data.remote.datasource.GyeonggiDataSourceImpl
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
    fun provideAllAreaDataSource(
        dataSource: AllAreaDataSource
    ): AllAreaDataSourceImpl = AllAreaDataSourceImpl(dataSource)

    @Provides
    @Singleton
    fun provideGyeonggiDataSource(
        dataSource: GyeonggiDataSource
    ): GyeonggiDataSourceImpl = GyeonggiDataSourceImpl(dataSource)
}