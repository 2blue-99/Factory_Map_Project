package com.factory.domain.repo

import com.factory.domain.model.FactoryInfo
import kotlinx.coroutines.flow.Flow

interface FactoryRepository {
    /**
     * 목표 업체 이름 검색
     */
    suspend fun getTargetFactoryDao(targetName: String): Int

    suspend fun getFactoryDao(): Flow<List<FactoryInfo>>

    suspend fun getTargetFactoryDao(updateListId: List<Int>): List<FactoryInfo>

    suspend fun upsertFactoryDao(data: FactoryInfo)

    suspend fun upsertFactoryListDao(data: List<FactoryInfo>)

    suspend fun deleteFactoryDao(id: Int)

    suspend fun deleteAllFactoryDao()



    suspend fun remoteSync(factoryInfo: FactoryInfo)

    suspend fun localSync(): List<FactoryInfo>?
}