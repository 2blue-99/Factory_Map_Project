package com.example.data.local.di

import com.example.data.local.AppDatabase
import com.example.data.local.dao.FactoryDao
import com.example.data.local.dao.FilterDao
import com.example.data.local.dao.ReceiveDao
import com.example.data.local.dao.SendDao
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
    fun provideFactoryDao(database: AppDatabase): FactoryDao =
        database.factoryDao()

    @Singleton
    @Provides
    fun provideFilterDao(database: AppDatabase): FilterDao =
        database.filterDao()

    @Singleton
    @Provides
    fun provideReceiveDao(database: AppDatabase): ReceiveDao =
        database.receiveDao()

    @Singleton
    @Provides
    fun provideSendDao(database: AppDatabase): SendDao =
        database.sendDao()
}