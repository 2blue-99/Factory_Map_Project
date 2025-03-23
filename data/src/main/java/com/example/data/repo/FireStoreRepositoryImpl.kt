package com.example.data.repo

import com.example.data.datastore.UserDataStore
import com.example.data.remote.datasource.FireStoreDataSource
import com.example.domain.model.FactoryInfo
import com.example.domain.repo.FireStoreRepository
import javax.inject.Inject

class FireStoreRepositoryImpl @Inject constructor(
    private val fireStoreDataSource: FireStoreDataSource,
    private val userDataStore: UserDataStore
): FireStoreRepository {
    override suspend fun login(id: String, passWord: String): Boolean {
        val userCode = fireStoreDataSource.login(id, passWord)

        return if(userCode.isNotBlank()){
            userDataStore.setUserCode(userCode)
            true
        }else{
            false
        }
    }

    override suspend fun logout() {
        userDataStore.setUserCode("")
    }

    override suspend fun insertRemoteFactory(factoryInfo: FactoryInfo) {

    }

    override suspend fun getRemoteFactory(): Boolean {
        return true
    }

}