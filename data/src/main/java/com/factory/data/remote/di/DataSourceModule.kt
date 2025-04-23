package com.factory.data.remote.di

import com.factory.data.remote.datasource.FireStoreDataSource
import com.factory.data.remote.datasource.FireStoreDataSourceImpl
import com.google.firebase.auth.FirebaseAuth
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
        firestore: FirebaseFirestore,
        firestoreAuth: FirebaseAuth
    ): FireStoreDataSource = FireStoreDataSourceImpl(firestore, firestoreAuth)

}