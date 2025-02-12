package com.example.domain.repo

import com.example.domain.model.FactoryInfo
import com.example.domain.model.FilterData
import com.example.domain.util.ResourceState
import com.example.domain.model.GyeonggiInfo
import kotlinx.coroutines.flow.Flow

interface FilterRepository {
    fun getAllFilterDao(): Flow<List<FilterData>>

    suspend fun upsertFilterDao(data: FilterData)

    suspend fun deleteFilterDao(id: Int)
}