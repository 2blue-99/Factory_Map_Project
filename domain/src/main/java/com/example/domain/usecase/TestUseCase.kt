package com.example.domain.usecase

import com.example.domain.util.ResourceState
import com.example.domain.model.AllAreaInfo
import com.example.domain.model.GyeonggiInfo
import com.example.domain.repo.TestRepository
import kotlinx.coroutines.flow.Flow

class TestUseCase(
    private val repo: TestRepository,
)  {
    suspend fun getAllAreaData(): Flow<ResourceState<List<AllAreaInfo>>> {
        return repo.getAllAreaData()
    }

    suspend fun getGyeonggiData(): Flow<ResourceState<List<GyeonggiInfo>>> {
        return repo.getGyeonggiData()
    }
}