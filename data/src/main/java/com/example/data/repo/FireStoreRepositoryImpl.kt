package com.example.data.repo

import com.example.domain.model.FactoryInfo
import com.example.domain.repo.FireStoreRepository
import javax.inject.Inject

class FireStoreRepositoryImpl @Inject constructor(): FireStoreRepository {
    override fun login() {}

    override suspend fun insertRemoteFactory(factoryInfo: FactoryInfo) {

    }

    override suspend fun getRemoteFactory(): Boolean {
        return true
    }

}