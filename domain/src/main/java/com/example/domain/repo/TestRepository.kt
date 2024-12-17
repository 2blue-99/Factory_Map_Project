package com.example.domain.repo

import com.example.domain.util.ResourceState
import com.example.domain.model.TestData
import kotlinx.coroutines.flow.Flow

interface TestRepository {

    suspend fun getTestData(): Flow<ResourceState<TestData>>

    fun getTestDao(): Flow<List<TestData>>

    fun upsertTestDao(testEntity: TestData)

    fun deleteTestDao(id: Int)
}