package com.factory.data.remote.di

import com.factory.data.remote.datasource.GyeonggiDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object APIModule {

//    @Singleton
//    @Provides
//    fun provideAllRetrofit(@RetrofitModule.All retrofit: Retrofit): AllAreaDataSource =
//        retrofit.create(AllAreaDataSource::class.java)

    @Singleton
    @Provides
    fun provideGyeonggiRetrofit(@RetrofitModule.Gyeonggi retrofit: Retrofit): GyeonggiDataSource =
        retrofit.create(GyeonggiDataSource::class.java)
}