package com.example.domain.repo

import com.example.domain.util.ResourceState
import com.example.domain.model.FactoryInfo
import kotlinx.coroutines.flow.Flow

interface TestRepository {

    suspend fun getTestData(): Flow<ResourceState<List<FactoryInfo>>>

    fun getTestDao(): Flow<List<FactoryInfo>>

    fun upsertTestDao(testEntity: FactoryInfo)

    fun deleteTestDao(id: Int)

    fun deleteAllDao()
}