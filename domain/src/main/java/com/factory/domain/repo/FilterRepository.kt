package com.factory.domain.repo

import com.factory.domain.model.FilterData
import kotlinx.coroutines.flow.Flow

interface FilterRepository {
    fun getAllFilterDao(): Flow<List<FilterData>>

    suspend fun upsertFilterDao(data: FilterData)

    suspend fun deleteFilterDao(id: Int)
}