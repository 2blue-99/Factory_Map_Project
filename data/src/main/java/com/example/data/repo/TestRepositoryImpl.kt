package com.example.data.repo

import com.example.data.local.dao.TestDao
import com.example.data.remote.TestDataSourceImpl
import com.example.data.remote.util.Mapper.toEntity
import com.example.data.remote.util.toDomainFlow
import com.example.domain.model.FactoryInfo
import com.example.domain.repo.TestRepository
import com.example.domain.util.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val dataSource: TestDataSourceImpl,
    private val testDao: TestDao
): TestRepository {
    override suspend fun getTestData(): Flow<ResourceState<List<FactoryInfo>>> {
        return dataSource.getDataSource().toDomainFlow { data ->
            data.response.body.items.item.map {
                it.mapper(
//                    totalCount = data.response.body.totalCount
                )
            }
        }
    }

    override fun getTestDao(): Flow<List<FactoryInfo>> {
        return testDao.getAllData().map { it.map { data -> data.toDomain() } }
    }

    override fun upsertTestDao(testEntity: FactoryInfo) {
        testDao.upsertData(testEntity.toEntity())
    }

    override fun deleteTestDao(id: Int) {
        testDao.deleteData(id)
    }

    override fun deleteAllDao() {
        testDao.deleteAllData()
    }
}