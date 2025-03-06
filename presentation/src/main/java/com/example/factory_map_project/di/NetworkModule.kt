package com.example.factory_map_project.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.data.util.NetworkInterface
import com.example.factory_map_project.util.NetworkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Singleton
    @Provides
    fun provideNetworkUtil(connectivityManager: ConnectivityManager): NetworkInterface = NetworkUtil(connectivityManager)
}