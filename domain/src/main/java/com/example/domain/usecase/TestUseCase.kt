package com.example.domain.usecase

import com.example.domain.util.ResourceState
import com.example.domain.model.TestData
import com.example.domain.repo.TestRepository
import kotlinx.coroutines.flow.Flow

class TestUseCase(
    private val repo: TestRepository,
)  {
    suspend fun test(): Flow<ResourceState<TestData>> {
        return repo.getTestData()
    }
}