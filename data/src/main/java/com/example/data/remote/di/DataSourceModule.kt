package com.example.data.remote.di

import com.example.data.remote.datasource.FireStoreDataSource
import com.example.data.remote.datasource.FireStoreDataSourceImpl
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideFireStoreDataSource(
        fireStore: FirebaseFirestore
    ): FireStoreDataSource = FireStoreDataSourceImpl(fireStore)

}