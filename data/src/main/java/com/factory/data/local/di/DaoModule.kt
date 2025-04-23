package com.factory.data.local.di

import com.factory.data.local.AppDatabase
import com.factory.data.local.dao.FactoryDao
import com.factory.data.local.dao.FilterDao
import com.factory.data.local.dao.ReceiveDao
import com.factory.data.local.dao.SendDao
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