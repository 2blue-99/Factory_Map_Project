package com.example.domain.repo

import com.example.domain.model.FactoryInfo
import com.example.domain.util.ResourceState
import com.example.domain.model.GyeonggiInfo
import kotlinx.coroutines.flow.Flow

interface FactoryRepository {

    fun getGyeonggiData(): Flow<ResourceState<List<GyeonggiInfo>>>

    fun saveGyeonggiData(): Flow<ResourceState<Int>>

    fun getGyeonggiDaoData(): Flow<List<FactoryInfo>>

    fun deleteTestDao(id: Int)

    fun deleteAllDao()
}