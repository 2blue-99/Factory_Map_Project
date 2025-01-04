package com.example.data.repo.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.data.datastore.UserDataSource
import com.example.data.local.dao.FactoryDao
import com.example.data.remote.datasource.AllAreaDataSourceImpl
import com.example.data.remote.datasource.GyeonggiDataSourceImpl
import com.example.data.repo.TestRepositoryImpl
import com.example.domain.repo.DataStoreRepo
import com.example.domain.repo.TestRepository
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
        allAreaDatasource: AllAreaDataSourceImpl,
        gyeonggiDatasource: GyeonggiDataSourceImpl,
        factoryDao: FactoryDao,
    ): TestRepository = TestRepositoryImpl(allAreaDatasource, gyeonggiDatasource, factoryDao)

    @Provides
    @Singleton
    fun provideDataStoreRepo(
        dataStore: DataStore<Preferences>
    ): DataStoreRepo = UserDataSource(dataStore)
}