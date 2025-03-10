package com.example.data.repo.di

import com.example.data.datastore.UserDataStore
import com.example.data.local.dao.FactoryDao
import com.example.data.local.dao.FilterDao
import com.example.data.local.dao.ReceiveDao
import com.example.data.local.dao.SendDao
import com.example.data.remote.datasource.FireStoreDataSource
import com.example.data.repo.FactoryRepositoryImpl
import com.example.data.repo.FilterRepositoryImpl
import com.example.data.repo.FireStoreRepositoryImpl
import com.example.data.util.NetworkInterface
import com.example.domain.repo.FactoryRepository
import com.example.domain.repo.FilterRepository
import com.example.domain.repo.FireStoreRepository
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
        receiveDao: ReceiveDao,
        sendDao: SendDao,
        userDataStore: UserDataStore
    ): FireStoreRepository = FireStoreRepositoryImpl()

}