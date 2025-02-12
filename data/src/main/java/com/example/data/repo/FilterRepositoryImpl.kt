package com.example.data.repo

import com.example.data.Mapper.toEntity
import com.example.data.local.dao.FilterDao
import com.example.domain.model.FilterData
import com.example.domain.repo.FilterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor(
    private val filterDao: FilterDao
): FilterRepository {
    override fun getAllFilterDao(): Flow<List<FilterData>> {
        return filterDao.getAllData().map { it.map { it.toDomain() } }
    }

    override suspend fun upsertFilterDao(data: FilterData) {
        filterDao.upsertData(data.toEntity())
    }

    override suspend fun deleteFilterDao(id: Int) {
        filterDao.deleteData(id)
    }
}