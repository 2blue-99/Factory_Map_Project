package com.example.data.repo

import com.example.data.Mapper.toEntity
import com.example.data.datastore.UserDataStore
import com.example.data.local.dao.FactoryDao
import com.example.data.local.dao.FilterDao
import com.example.data.remote.datasource.GyeonggiDataSourceImpl
import com.example.data.remote.util.toDomain
import com.example.domain.model.FactoryInfo
import com.example.domain.model.GyeonggiInfo
import com.example.domain.repo.FactoryRepository
import com.example.domain.type.AreaType
import com.example.domain.util.GYEONGGI_DOWNLOAD_COUNT
import com.example.domain.util.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FactoryRepositoryImpl @Inject constructor(
    private val gyeonggiDataSource: GyeonggiDataSourceImpl,
    private val factoryDao: FactoryDao,
    private val filterDao: FilterDao,
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
            userDataSource.setDownload(true)
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

// ("A", "1") ("A", "2") ("A", "3") ("B", "4") ("B", "5")
// [("A", ["1","2","3"]), ("B", ["4","5"])]
    override suspend fun getFactoryDao(): Flow<List<FactoryInfo>> {

        val targetArea = AreaType.toType(userDataSource.areaPositionFlow.first()).title
        val filterList = filterDao.getAllData().first()
        val targetFilterList = filterList.map { it.target }.toSet().toList()
        val gap = filterList.filter { it.target == targetFilterList[0] }.map { it.keyword }

        val mapList = filterList.map { it.target }
            .toSet()
            .map { key ->
                key to filterList.filter { it.target == key }.map { it.target }
            }
        for((key, list) in mapList){
            key
        }


        val keywordFilterList = filterList.map { it.keyword }
        val entireList = factoryDao.getTargetData(targetArea).map { it.map { it.toDomain() } }


        return entireList
    }

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