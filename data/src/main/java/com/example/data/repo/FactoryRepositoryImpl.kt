package com.example.data.repo

import com.example.data.Mapper.toEntity
import com.example.data.Mapper.toResponse
import com.example.data.datastore.UserDataStore
import com.example.data.local.dao.FactoryDao
import com.example.data.local.dao.FilterDao
import com.example.data.local.dao.ReceiveDao
import com.example.data.local.dao.SendDao
import com.example.data.local.entity.SendEntity
import com.example.data.remote.datasource.FireStoreDataSource
import com.example.data.remote.model.FactoryResponse
import com.example.data.remote.model.toEntity
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
    private val fireStoreDataSource: FireStoreDataSource,
    private val userDataSource: UserDataStore,
    private val factoryDao: FactoryDao,
    private val filterDao: FilterDao,
    private val sendDao: SendDao,
    private val receiveDao: ReceiveDao,
    private val networkUtil: NetworkInterface,
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

    /**
     * 1. Local DB 업데이트
     *
     * 네트워크 연결 이라면,
     * 2. Sync 작업
     *
     */
    override suspend fun upsertFactoryDao(data: FactoryInfo) {
        factoryDao.upsertData(data.toEntity())
        if(networkUtil.networkState.first()){
            remoteSync(data)
        }
    }

    override suspend fun deleteFactoryDao(id: Int) {
        factoryDao.deleteData(id)
    }

    override suspend fun deleteAllFactoryDao() {
        factoryDao.deleteAllData()
    }


    /**
     * Local 변경 사항을 Remote 에 반영
     */
    private suspend fun remoteSync(factoryInfo: FactoryInfo){
        sendDao.upsertData(
            SendEntity(
            sendId = 0,
            factory = factoryInfo.toEntity(),
            isUpdate = false
        )
        )

        val notUpdateList = sendDao.getNotUpdateData()

        val sendList = notUpdateList.map {
            FactoryResponse(
                userCode = userDataSource.userCodeFlow.first(),
                factory = factoryInfo.toResponse()
            )
        }

        if(fireStoreDataSource.insertData(sendList)){
            Timber.d("Remote Update 성공")
            notUpdateList.forEach {
                sendDao.upsertData(it.copy(isUpdate = true))
            }
        }else{
            Timber.d("Remote Update 실패")
        }
    }

    /**
     * Remote 변경 사항을 Local 에 반영
     *
     * true  : Local DB와 동기화 필요 + 비교 후 반영 작업 필요
     * false : 동기화 불필요
     */
    suspend fun localSync(): Boolean {
        try {
            val existDataList = receiveDao.getAllData().first()
            val existIdList = existDataList.map { it.remoteId }

            val remoteList = fireStoreDataSource.getAllData()

            if(existIdList.size == remoteList.size){
                Timber.d("동기화 할 항목이 없습니다.")
                return false
            }

            val filterRemoteList = remoteList.filter { !existIdList.contains(it.first) }

            filterRemoteList.forEach { data ->
                receiveDao.upsertData(data.toEntity())
            }

            return true
        }catch (e: Exception){
            Timber.d("err : $e")
            return false
        }
    }

}