package com.example.data.repo

import com.example.data.local.dao.FactoryDao
import com.example.data.remote.datasource.GyeonggiDataSourceImpl
import com.example.data.remote.util.toDomain
import com.example.domain.model.GyeonggiInfo
import com.example.domain.repo.TestRepository
import com.example.domain.util.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val gyeonggiDataSource: GyeonggiDataSourceImpl,
    private val factoryDao: FactoryDao
): TestRepository {
    override fun getGyeonggiData(): Flow<ResourceState<List<GyeonggiInfo>>> {
        return flow {
            emit(ResourceState.Loading())
            for(index in 1..10) {
                emit(
                    gyeonggiDataSource.getDataSource(index).toDomain { data ->
                        data.factoryRegistTm[1].rows.map { it.toDomain() }
                    })
            }
        }
    }

    override fun saveGyeonggiData(): Flow<ResourceState<Int>> {
        return flow {
            emit(ResourceState.Loading())
            for(index in 1..5) {
                val result = gyeonggiDataSource.getDataSource(index).toDomain { data ->
                    data.factoryRegistTm[1].rows.map { it.toEntity() }
                }
                val dataResource = when(result){
                    is ResourceState.Success -> {
                        withContext(Dispatchers.IO){
                            factoryDao.upsertDataList(result.body)
                        }
                        ResourceState.Success(result.code, result.message, index)
                    }
                    is ResourceState.Failure -> ResourceState.Failure(result.code, result.message)
                    is ResourceState.Exception -> ResourceState.Exception(result.type)
                    is ResourceState.Loading -> ResourceState.Loading()
                }
                emit(dataResource)
            }
        }
    }

    override fun getGyeonggiDaoData(): Flow<List<GyeonggiInfo>> {
        return factoryDao.getAllData().map { it.map { it.toDomain() } }
    }


    override fun deleteTestDao(id: Int) {
        factoryDao.deleteData(id)
    }

    override fun deleteAllDao() {
        factoryDao.deleteAllData()
    }
}