package com.example.data.repo.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.data.datastore.UserDataStore
import com.example.data.datastore.UserDataStoreImpl
import com.example.data.local.dao.FactoryDao
import com.example.data.local.dao.FilterDao
import com.example.data.remote.datasource.FirebaseDataSource
import com.example.data.remote.datasource.FirebaseDataSourceImpl
import com.example.data.remote.datasource.GyeonggiDataSourceImpl
import com.example.data.repo.FactoryRepositoryImpl
import com.example.data.repo.FilterRepositoryImpl
import com.example.domain.repo.FactoryRepository
import com.example.domain.repo.FilterRepository
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
        gyeonggiDatasource: GyeonggiDataSourceImpl,
        firebaseDataSource: FirebaseDataSource,
        factoryDao: FactoryDao,
        filterDao: FilterDao,
        userDataStore: UserDataStore
    ): FactoryRepository = FactoryRepositoryImpl(gyeonggiDatasource, firebaseDataSource, factoryDao, filterDao, userDataStore)

    @Provides
    @Singleton
    fun provideFilterRepo(
        filterDao: FilterDao,
    ): FilterRepository = FilterRepositoryImpl(filterDao)

}