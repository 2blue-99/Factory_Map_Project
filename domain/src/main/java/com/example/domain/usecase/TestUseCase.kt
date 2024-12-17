package com.example.domain.usecase

import com.example.domain.util.ResourceState
import com.example.domain.model.FactoryInfo
import com.example.domain.repo.TestRepository
import kotlinx.coroutines.flow.Flow

class TestUseCase(
    private val repo: TestRepository,
)  {
    suspend fun test(): Flow<ResourceState<List<FactoryInfo>>> {
        return repo.getTestData()
    }
}