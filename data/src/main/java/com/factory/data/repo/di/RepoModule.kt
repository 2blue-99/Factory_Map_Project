package com.factory.data.repo.di

import com.factory.data.datastore.UserDataStore
import com.factory.data.local.dao.FactoryDao
import com.factory.data.local.dao.FilterDao
import com.factory.data.local.dao.ReceiveDao
import com.factory.data.local.dao.SendDao
import com.factory.data.remote.datasource.FireStoreDataSource
import com.factory.data.repo.FactoryRepositoryImpl
import com.factory.data.repo.FilterRepositoryImpl
import com.factory.data.repo.FireStoreRepositoryImpl
import com.factory.data.util.NetworkInterface
import com.factory.domain.repo.FactoryRepository
import com.factory.domain.repo.FilterRepository
import com.factory.domain.repo.FireStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    @Singleton
    fun provideRepo(
        fireStoreDataSource: FireStoreDataSource,
        userDataStore: UserDataStore,
        factoryDao: FactoryDao,
        filterDao: FilterDao,
        sendDao: SendDao,
        receiveDao: ReceiveDao,
        networkUtil: NetworkInterface
    ): FactoryRepository = FactoryRepositoryImpl(fireStoreDataSource, userDataStore, factoryDao, filterDao, sendDao, receiveDao, networkUtil)

    @Provides
    @Singleton
    fun provideFilterRepo(
        filterDao: FilterDao,
    ): FilterRepository = FilterRepositoryImpl(filterDao)

    @Provides
    @Singleton
    fun provideFireStoreRepo(
        fireStoreDataSource: FireStoreDataSource,
        userDataStore: UserDataStore
    ): FireStoreRepository = FireStoreRepositoryImpl(fireStoreDataSource, userDataStore)

}