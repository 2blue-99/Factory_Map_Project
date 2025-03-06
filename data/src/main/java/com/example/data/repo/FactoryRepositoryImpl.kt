package com.example.data.repo

import com.example.data.Mapper.toEntity
import com.example.data.datastore.UserDataStore
import com.example.data.local.dao.FactoryDao
import com.example.data.local.dao.FilterDao
import com.example.data.util.NetworkInterface
import com.example.domain.model.FactoryInfo
import com.example.domain.repo.FactoryRepository
import com.example.domain.repo.FireStoreRepository
import com.example.domain.type.AreaType
import com.example.domain.type.SelectType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class FactoryRepositoryImpl @Inject constructor(
    private val fireStoreRepository: FireStoreRepository,
    private val factoryDao: FactoryDao,
    private val filterDao: FilterDao,
    private val userDataSource: UserDataStore,
    private val networkUtil: NetworkInterface
): FactoryRepository {
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
        if(networkUtil.networkState.first()){
            fireStoreRepository.insertRemoteFactory(data)
        }
    }

    override suspend fun deleteFactoryDao(id: Int) {
        factoryDao.deleteData(id)
    }

    override suspend fun deleteAllFactoryDao() {
        factoryDao.deleteAllData()
    }
}