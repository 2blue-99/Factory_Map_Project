package com.example.domain.repo

import com.example.domain.util.ResourceState
import com.example.domain.model.AllAreaInfo
import com.example.domain.model.GyeonggiInfo
import kotlinx.coroutines.flow.Flow

interface TestRepository {

    fun getGyeonggiData(): Flow<ResourceState<List<GyeonggiInfo>>>

    fun saveGyeonggiData(): Flow<ResourceState<Int>>

    fun deleteTestDao(id: Int)

    fun deleteAllDao()
}