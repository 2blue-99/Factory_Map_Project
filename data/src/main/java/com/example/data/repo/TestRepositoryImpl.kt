package com.example.data.repo

import com.example.data.local.dao.TestDao
import com.example.data.remote.datasource.AllAreaDataSourceImpl
import com.example.data.remote.datasource.GyeonggiDataSourceImpl
import com.example.data.remote.model.GyeonggiResponse
import com.example.data.remote.util.APIResponseState
import com.example.data.remote.util.Mapper.toEntity
import com.example.data.remote.util.toDomainFlow
import com.example.domain.model.AllAreaInfo
import com.example.domain.model.GyeonggiInfo
import com.example.domain.repo.TestRepository
import com.example.domain.util.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val allAreaDataSource: AllAreaDataSourceImpl,
    private val gyeonggiDataSource: GyeonggiDataSourceImpl,
    private val testDao: TestDao
): TestRepository {
    override suspend fun getAllAreaData(): Flow<ResourceState<List<AllAreaInfo>>> {
        return allAreaDataSource.getDataSource().toDomainFlow { data ->
            data.response.body.items.item.map { it.mapper() }
        }
    }

    override suspend fun getGyeonggiData(): Flow<ResourceState<List<GyeonggiInfo>>> {
        return (1..1).asFlow()  // Flow로 1부터 100까지 생성
            .map { page ->  // 각 page에 대해 getDataSource 호출
                gyeonggiDataSource.getDataSource(page).toDomainFlow { data ->
                    // 각 데이터에서 rows를 가져와서 mapping
                    data.factoryRegistTm[1].rows.map { it.mapper() }
                }
            }
            .flattenMerge()  // 여러 Flow를 병합하여 순차적으로 처리
    }

    override fun getTestDao(): Flow<List<AllAreaInfo>> {
        return testDao.getAllData().map { it.map { data -> data.toDomain() } }
    }

    override fun upsertTestDao(testEntity: AllAreaInfo) {
        testDao.upsertData(testEntity.toEntity())
    }

    override fun deleteTestDao(id: Int) {
        testDao.deleteData(id)
    }

    override fun deleteAllDao() {
        testDao.deleteAllData()
    }
}