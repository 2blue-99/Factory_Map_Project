package com.example.data.repo

import com.example.data.Mapper.toEntity
import com.example.data.datastore.UserDataStore
import com.example.data.local.dao.FactoryDao
import com.example.data.local.dao.FilterDao
import com.example.data.local.entity.FactoryEntity
import com.example.data.remote.datasource.FirebaseDataSource
import com.example.data.remote.datasource.GyeonggiDataSourceImpl
import com.example.data.remote.util.toDomain
import com.example.domain.model.FactoryInfo
import com.example.domain.model.GyeonggiInfo
import com.example.domain.repo.FactoryRepository
import com.example.domain.type.AreaType
import com.example.domain.type.SelectType
import com.example.domain.util.GYEONGGI_DOWNLOAD_COUNT
import com.example.domain.util.ResourceState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FactoryRepositoryImpl @Inject constructor(
    private val gyeonggiDataSource: GyeonggiDataSourceImpl,
    private val firebaseDataSource: FirebaseDataSource,
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
                    data.factoryRegistTm[1].rows.map { it as FactoryEntity }
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
        return combine(userDataSource.areaPositionFlow, userDataSource.currentLocationFlow, filterDao.getAllData()) { areaPosition, mapInfo, filterList ->
            Timber.d("areaPosition : $areaPosition / mapInfo : $mapInfo, filterList : $filterList")
            Triple(AreaType.toType(areaPosition).title,mapInfo, filterList)
        }.distinctUntilChanged()
            .flatMapLatest { (areaTitle, mapInfo, filterList) ->
            Timber.d("areaPosition : $areaTitle / mapInfo : $mapInfo, filterList : $filterList")
            val excludeCompany =
                filterList.filter { it.target == SelectType.COMPANY.title }.map { it.keyword }
            val excludeBusinessType =
                filterList.filter { it.target == SelectType.BUSINESS_TYPE.title }.map { it.keyword }
            val excludeProduct =
                filterList.filter { it.target == SelectType.PRODUCT.title }.map { it.keyword }

            Timber.d("excludeCompany : $excludeCompany")
            Timber.d("excludeCategory : $excludeBusinessType")
            Timber.d("excludeProduct : $excludeProduct")
            Timber.d("location.first : ${mapInfo.first}")
            Timber.d("location.second : ${mapInfo.second}")
            Timber.d("location.second : ${mapInfo.third}")

            val range = if(areaTitle == AreaType.ALL.title) mapInfo.third else 5.0
            val searchArea = if(areaTitle == AreaType.ALL.title) "" else areaTitle

            factoryDao.getTargetData(searchArea, mapInfo.first, mapInfo.second, range).map { list ->
                list.filter { data ->
                    excludeCompany.none { keyword -> data.companyName.contains(keyword) && !data.isCheck }
                }.filter { data ->
                    excludeBusinessType.none { keyword -> data.businessType.contains(keyword) && !data.isCheck }
                }.filter { data ->
                    excludeProduct.none { keyword -> data.product.contains(keyword) && !data.isCheck }
                }.map {
                    it.toDomain()
                }
            }

        }
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

    override fun insertRemoteFactory() {
        firebaseDataSource.insertData()
    }


    override fun getRemoteFactory() {
        firebaseDataSource.getAllData()
    }
}