package com.example.data.repo.di

import com.example.domain.repo.TestRepository
import com.example.domain.usecase.TestDaoUseCase
import com.example.domain.usecase.TestUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun bindTestUseCase(repo: TestRepository): TestUseCase = TestUseCase(repo)
    @Provides
    @Singleton
    fun bindDaoTestUseCase(repo: TestRepository): TestDaoUseCase = TestDaoUseCase(repo)
}