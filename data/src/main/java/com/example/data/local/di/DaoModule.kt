package com.example.data.local.di

import com.example.data.local.AppDatabase
import com.example.data.local.dao.FactoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Singleton
    @Provides
    fun provideTestDao(database: AppDatabase): FactoryDao =
        database.testDao()
}