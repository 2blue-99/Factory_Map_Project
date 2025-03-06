package com.example.data.repo

import com.example.data.Mapper.toEntity
import com.example.data.Mapper.toResponse
import com.example.data.datastore.DataStore
import com.example.data.local.dao.ReceiveDao
import com.example.data.local.dao.SendDao
import com.example.data.local.entity.SendEntity
import com.example.data.remote.datasource.FireStoreDataSource
import com.example.data.remote.model.FactoryResponse
import com.example.domain.model.FactoryInfo
import com.example.domain.repo.FireStoreRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class FireStoreRepositoryImpl @Inject constructor(
    private val fireStoreDataSource: FireStoreDataSource,
    private val receiveDao: ReceiveDao,
    private val sendDao: SendDao,
    private val userDataSource: DataStore

): FireStoreRepository {
    override fun login() {}

    override suspend fun insertRemoteFactory(factoryInfo: FactoryInfo) {
        sendDao.upsertData(SendEntity(
            sendId = 0,
            factory = factoryInfo.toEntity(),
            isUpdate = false
        ))

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

    override suspend fun getRemoteFactory() {

    }

}