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
        return combine(
            userDataSource.areaPositionFlow,
            userDataSource.currentLocationFlow,
            filterDao.getAllData()
        ) { areaPosition, mapInfo, filterList ->
            Timber.d("areaPosition : $areaPosition / mapInfo : $mapInfo, filterList : $filterList")
            Triple(AreaType.toType(areaPosition).title, mapInfo, filterList)
        }.distinctUntilChanged()
            .flatMapLatest { (areaTitle, mapInfo, filterList) ->
                Timber.d("areaPosition : $areaTitle / mapInfo : $mapInfo, filterList : $filterList")
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
                Timber.d("location.first : ${mapInfo.first}")
                Timber.d("location.second : ${mapInfo.second}")
                Timber.d("location.second : ${mapInfo.third}")

                val range = if (areaTitle == AreaType.ALL.title) mapInfo.third else 5.0
                val searchArea = if (areaTitle == AreaType.ALL.title) "" else areaTitle

                factoryDao.getTargetData(searchArea, mapInfo.first, mapInfo.second, range)
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
     * 0. 네트워크 미연결 시, emptyList 반환
     * 1. Receive DB 전체 조회
     * 2. Remote DB 전체 조회
     * 3. 사이즈가 같을 경우 emptyList 반환 (동기화 불필요)
     * 4. 사이즈가 다를 경우 Remote ID != Receive DB ID 필터링
     * 5. 필터링 데이터 Receive DB 에 업데이트
     * 6. FactoryInfo 로 형변환 후 반환
     * --> 사용자 데이터 지정 후 Local DB에 반영
     */
    override suspend fun localSync(): List<FactoryInfo> {
        if(!networkUtil.networkState.value){
            return emptyList()
        }

        val existDataList = receiveDao.getAllData().first()
        val existIdList = existDataList.map { it.remoteId }

        val remoteList = fireStoreDataSource.getAllData()

        if (existIdList.size == remoteList.size) {
            Timber.d("동기화 할 항목이 없습니다.")
            return emptyList()
        }

        val filterRemoteList = remoteList.filter { !existIdList.contains(it.first) }

        filterRemoteList.forEach { data ->
            receiveDao.upsertData(data.toEntity())
        }

        return filterRemoteList.map { it.second.factory.toDomain() }
    }
}