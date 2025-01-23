package com.example.data.repo.di

import com.example.domain.repo.FactoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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