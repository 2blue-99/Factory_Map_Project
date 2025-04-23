package com.factory.domain.repo

import com.factory.domain.model.FactoryInfo

interface FireStoreRepository {
    suspend fun login(id: String, passWord: String): Boolean

    suspend fun logout()

    suspend fun insertRemoteFactory(factoryInfo: FactoryInfo)

    suspend fun getRemoteFactory()
}