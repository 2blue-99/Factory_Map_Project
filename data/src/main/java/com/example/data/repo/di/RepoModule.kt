package com.example.data.repo.di

import android.service.autofill.UserData
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.data.datastore.UserDataSource
import com.example.data.local.dao.TestDao
import com.example.data.remote.TestDataSourceImpl
import com.example.data.remote.TestDataSourceImpl_Factory
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
        dataSource: TestDataSourceImpl,
        testDao: TestDao,
    ): TestRepository = TestRepositoryImpl(dataSource, testDao)

    @Provides
    @Singleton
    fun provideDataStoreRepo(
        dataStore: DataStore<Preferences>
    ): DataStoreRepo = UserDataSource(dataStore)
}