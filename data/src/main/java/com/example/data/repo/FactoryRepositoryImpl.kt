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
import com.example.domain.type.SelectType
import com.example.domain.util.GYEONGGI_DOWNLOAD_COUNT
import com.example.domain.util.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
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

    override suspend fun getFactoryDao(): Flow<List<FactoryInfo>> {
        return userDataSource.areaPositionFlow.flatMapLatest { areaPosition ->
            val targetArea = AreaType.toType(areaPosition).title
            Timber.d("targetArea : $targetArea")

            val filterList = filterDao.getAllData().first()

            val excludeCompany =
                filterList.filter { it.target == SelectType.COMPANY.title }.map { it.keyword }
            val excludeCategory =
                filterList.filter { it.target == SelectType.CATEGORY.title }.map { it.keyword }
            val excludeProduct =
                filterList.filter { it.target == SelectType.PRODUCT.title }.map { it.keyword }

            Timber.d("excludeCompany : $excludeCompany")
            Timber.d("excludeCategory : $excludeCategory")

            factoryDao.getTargetData(targetArea).map { list ->
                list.filter { data ->
                    excludeCompany.none { keyword -> data.companyName.contains(keyword) }
                    excludeCategory.none { keyword -> data.category.contains(keyword) }
                    excludeProduct.none { keyword -> data.productInfo.contains(keyword) }
                }.map {
                    it.toDomain()
                }.apply {
                    Timber.d("repo list size : ${this.size}")
                }
            }
        }
    }

//    override suspend fun getFactoryDao(): Flow<List<FactoryInfo>> {
//        val targetArea = AreaType.toType(userDataSource.areaPositionFlow.first()).title
//        val filterList = filterDao.getAllData().first()
//
//        val excludeCompany = filterList.filter { it.target == SelectType.COMPANY.title }.map { it.keyword }
//        val excludeCategory = filterList.filter { it.target == SelectType.CATEGORY.title }.map { it.keyword }
//        val excludeProduct = filterList.filter { it.target == SelectType.PRODUCT.title }.map { it.keyword }
//
//        Timber.d("excludeCompany : $excludeCompany")
//        Timber.d("excludeCategory : $excludeCategory")
//
//        val entireList = factoryDao.getTargetData(targetArea).map {
//            it.filter { data ->
//                excludeCompany.none { keyword -> data.companyName.contains(keyword) }
//            }.filter { data ->
//                excludeCategory.none { keyword -> data.category.contains(keyword) }
//            }.filter { data ->
//                excludeProduct.none { keyword -> data.productInfo.contains(keyword) }
//            }.map {
//                it.toDomain()
//            }.apply {
//                Timber.d("size : ${it.size}")
//            }
//        }
//        return entireList
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