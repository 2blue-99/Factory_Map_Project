package com.factory.data.repo.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
//    @Provides
//    @Singleton
//    fun bindTestUseCase(repo: FactoryRepository): GetGyenggiUseCase = GetGyenggiUseCase(repo)
//    @Provides
//    @Singleton
//    fun bindDaoTestUseCase(repo: FactoryRepository): GyeonggiDaoUseCase = GyeonggiDaoUseCase(repo)
}