package com.example.data.repo

import com.example.data.Mapper.toEntity
import com.example.data.datastore.UserDataStore
import com.example.data.local.dao.FactoryDao
import com.example.data.remote.datasource.GyeonggiDataSourceImpl
import com.example.data.remote.util.toDomain
import com.example.domain.type.AreaType
import com.example.domain.model.FactoryInfo
import com.example.domain.model.GyeonggiInfo
import com.example.domain.repo.FactoryRepository
import com.example.domain.util.GYEONGGI_DOWNLOAD_COUNT
import com.example.domain.util.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FactoryRepositoryImpl @Inject constructor(
    private val gyeonggiDataSource: GyeonggiDataSourceImpl,
    private val factoryDao: FactoryDao,
    private val userDataSource: UserDataStore
): FactoryRepository {
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
            for(index in 1..GYEONGGI_DOWNLOAD_COUNT) {
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

    override fun getFactoryAllDao(): Flow<List<FactoryInfo>> {
        return factoryDao.getAllData().map { it.map { it.toDomain() } }
    }


    override suspend fun getFactoryDao(): Flow<List<FactoryInfo>> {
        val target = AreaType.toType(userDataSource.areaPositionFlow.first()).title
        Timber.d("target : $target")
        return factoryDao.getTargetData(target).map { it.map { it.toDomain() } }
    }

//    override fun getFactoryDao(): Flow<List<FactoryInfo>> {
//        return factoryDao.getAllData().map { it.map { it.toDomain() } }
//    }

    override suspend fun upsertFactoryDao(data: FactoryInfo) {
        factoryDao.upsertData(data.toEntity())
    }

    override suspend fun deleteFactoryDao(id: Int) {
        factoryDao.deleteData(id)
    }

    override suspend fun deleteAllFactoryDao() {
        factoryDao.deleteAllData()
    }
}