package com.example.domain.usecase

import com.example.domain.util.ResourceState
import com.example.domain.model.GyeonggiInfo
import com.example.domain.repo.FactoryRepository
import kotlinx.coroutines.flow.Flow

class SaveGyenggiUseCase(
    private val repo: FactoryRepository,
)  {
    fun getGyeonggiData(): Flow<ResourceState<List<GyeonggiInfo>>> {
        return repo.getGyeonggiData()
    }

    fun saveGyeonggiData(): Flow<ResourceState<Int>>{
        return repo.saveGyeonggiData()
    }
}