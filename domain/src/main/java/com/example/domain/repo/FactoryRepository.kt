package com.example.domain.repo

import com.example.domain.model.FactoryInfo
import com.example.domain.util.ResourceState
import com.example.domain.model.GyeonggiInfo
import kotlinx.coroutines.flow.Flow

interface FactoryRepository {

    fun getGyeonggiData(): Flow<ResourceState<List<GyeonggiInfo>>>

    fun saveGyeonggiData(): Flow<ResourceState<Int>>


    // DAO
    fun getFactoryAllDao(): Flow<List<FactoryInfo>>

    suspend fun getFactoryDao(): Flow<List<FactoryInfo>>

    suspend fun upsertFactoryDao(data: FactoryInfo)

    suspend fun deleteFactoryDao(id: Int)

    suspend fun deleteAllFactoryDao()

    fun insertRemoteFactory()
    fun getRemoteFactory()
}