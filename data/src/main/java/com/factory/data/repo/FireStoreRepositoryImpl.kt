package com.factory.data.repo

import com.factory.data.datastore.UserDataStore
import com.factory.data.remote.datasource.FireStoreDataSource
import com.factory.domain.model.FactoryInfo
import com.factory.domain.repo.FireStoreRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FireStoreRepositoryImpl @Inject constructor(
    private val fireStoreDataSource: FireStoreDataSource,
    private val userDataStore: UserDataStore
): FireStoreRepository {
    override suspend fun login(id: String, passWord: String): Boolean {
        val userCode = fireStoreDataSource.login(id, passWord)
        // 로그인 성공
        return if(userCode.isNotBlank()){
            // 자동 로그인 동의 시, 유저 코드 저장
            if(userDataStore.autoLoginFlow.first()){
                userDataStore.setUserCode(userCode)
            }
            true
        // 로그인 실패
        }else{
            false
        }
    }

    override suspend fun logout() {
        userDataStore.setUserCode("")
    }

    override suspend fun insertRemoteFactory(factoryInfo: FactoryInfo) {

    }

    override suspend fun getRemoteFactory() {
        fireStoreDataSource.getAllData(userDataStore.userCodeFlow.first())
    }

}