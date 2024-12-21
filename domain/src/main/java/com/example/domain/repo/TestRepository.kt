package com.example.domain.repo

import com.example.domain.util.ResourceState
import com.example.domain.model.AllAreaInfo
import com.example.domain.model.GyeonggiInfo
import kotlinx.coroutines.flow.Flow

interface TestRepository {

    suspend fun getAllAreaData(): Flow<ResourceState<List<AllAreaInfo>>>

    suspend fun getGyeonggiData(): Flow<ResourceState<List<GyeonggiInfo>>>

    fun getTestDao(): Flow<List<AllAreaInfo>>

    fun upsertTestDao(testEntity: AllAreaInfo)

    fun deleteTestDao(id: Int)

    fun deleteAllDao()
}