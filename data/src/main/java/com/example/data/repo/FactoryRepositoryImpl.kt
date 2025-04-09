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
import com.example.data.util.FactoryCombine
import com.example.domain.model.FactoryInfo
import com.example.domain.repo.FactoryRepository
import com.example.domain.type.AreaType
import com.example.domain.type.SelectType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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

    override suspend fun getTargetFactoryDao(targetName: String): Int {
        return factoryDao.getTargetFactoryDao(targetName)
    }

    override suspend fun getFactoryDao(): Flow<List<FactoryInfo>> {
        return combine(
            userDataSource.areaPositionFlow,
            userDataSource.currentLocationFlow,
            filterDao.getAllData(),
            userDataSource.searchNameFlow
        ) { areaPosition, mapInfo, filterList, searchName ->
            Timber.d("areaPosition : $areaPosition / mapInfo : $mapInfo, filterList : $filterList, searchName : $searchName")
            FactoryCombine(
                areaTitle = AreaType.toType(areaPosition).title,
                longitude = mapInfo.first,
                latitude = mapInfo.second,
                zoomLevel = mapInfo.third,
                filterList = filterList,
                searchName = searchName
            )
        }.distinctUntilChanged()
            .flatMapLatest { (areaTitle, longitude, latitude, zoomLevel, filterList, searchName) ->

                val excludeCompany =
                    filterList.filter { it.target == SelectType.COMPANY.title }.map { it.keyword }
                val excludeBusinessType =
                    filterList.filter { it.target == SelectType.BUSINESS_TYPE.title }
                        .map { it.keyword }
                val excludeProduct =
                    filterList.filter { it.target == SelectType.PRODUCT.title }.map { it.keyword }

                Timber.d("excludeCompany : $excludeCompany")
                Timber.d("excludeCategory : $excludeBusinessType")
                Timber.d("excludeProduct : $excludeProduct")
                Timber.d("longitude : ${longitude}") // 위도
                Timber.d("latitude : ${latitude}") // 경도
                Timber.d("zoomLevel : ${zoomLevel}") // 줌 레벨
                Timber.d("searchName : ${searchName}") // 줌 레벨

                val range = if (areaTitle == AreaType.ALL.title && searchName.isBlank()) zoomLevel else 5.0
                val searchArea = if (areaTitle == AreaType.ALL.title) "" else areaTitle

                factoryDao.getTargetData(searchArea, longitude, latitude, range, searchName)
                    .map { list ->
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

    override suspend fun getTargetFactoryDao(updateListId: List<Int>): List<FactoryInfo> {
        return factoryDao.getTargetIdData(updateListId).map { it.toDomain() }
    }

    /**
     * Local DB 업데이트
     * 네트워크 연결 시 Remote Sync 작업
     */
    override suspend fun upsertFactoryDao(data: FactoryInfo) {
        factoryDao.upsertData(data.toEntity())
        if (networkUtil.networkState.first()) {
            remoteSync(data)
        }
    }

    override suspend fun upsertFactoryListDao(data: List<FactoryInfo>) {
        factoryDao.upsertDataList(data.map { it.toEntity() })
    }

    override suspend fun deleteFactoryDao(id: Int) {
        factoryDao.deleteData(id)
    }

    override suspend fun deleteAllFactoryDao() {
        factoryDao.deleteAllData()
    }




    /**
     * Local 변경 사항을 Remote 에 반영
     *
     * 1. Send DB 추가
     * 2. Send DB Update == false 항목 조회
     * 3. Remote Data Class - FactoryResponse 로 변경
     * 4. Remote 업데이트
     * 5. 성공 시, Send DB Update = true 처리
     */
    override suspend fun remoteSync(factoryInfo: FactoryInfo) {
        Timber.d("remoteSync")
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

        Timber.d("sendList : $sendList")

        if (fireStoreDataSource.insertData(sendList)) {
            Timber.d("Remote Update 성공")
            notUpdateList.forEach {
                sendDao.upsertData(it.copy(isUpdate = true))
            }
        } else {
            Timber.d("Remote Update 실패")
        }
    }

    /**
     * Remote 변경사항을 Receive DB와 비교하여 업데이트 리스트 반환
     *
     * 0. 네트워크 미연결 시, null 반환
     * 1. Receive DB 전체 조회
     * 2. Remote DB 전체 조회
     * 3. 사이즈가 같을 경우 null 반환 (동기화 불필요)
     * 4. 사이즈가 다를 경우 Remote ID != Receive DB ID 필터링
     * 5. 필터링 데이터 Receive DB 에 업데이트
     * 6. FactoryInfo 로 형변환 후 반환
     * --> 사용자 데이터 지정 후 Local DB에 반영
     */
    override suspend fun localSync(): List<FactoryInfo>? {
        if(!networkUtil.networkState.value){
//            return emptyList()
            Timber.d("네트워크 미연결")
            return null
        }

        val existDataList = receiveDao.getAllData().first()
        val existIdList = existDataList.map { it.remoteId }

        val remoteList = fireStoreDataSource.getAllData(userDataSource.userCodeFlow.first())

        Timber.d("existIdList : $existIdList")
        Timber.d("remoteList : $remoteList")

        if (existIdList.size == remoteList.size) {
            Timber.d("동기화 할 항목이 없습니다.")
//            return emptyList()
            return null
        }

        val filterRemoteList = remoteList.filter { !existIdList.contains(it.first) }

        filterRemoteList.forEach { data ->
            withContext(Dispatchers.IO){
                receiveDao.upsertData(data.toEntity())
            }
        }

        return filterRemoteList.map { it.second.factory!!.toDomain() }
    }
}