package com.example.domain.repo

import com.example.domain.model.FactoryInfo
import kotlinx.coroutines.flow.Flow

interface FactoryRepository {
    // DAO
    fun getFactoryAllDao(): Flow<List<FactoryInfo>>

    suspend fun getFactoryDao(): Flow<List<FactoryInfo>>

    suspend fun upsertFactoryDao(data: FactoryInfo)

    suspend fun deleteFactoryDao(id: Int)

    suspend fun deleteAllFactoryDao()
}