package com.example.domain.repo

import com.example.domain.model.FactoryInfo

interface FireStoreRepository {
    suspend fun login(id: String, passWord: String): Boolean

    suspend fun logout()

    suspend fun insertRemoteFactory(factoryInfo: FactoryInfo)

    suspend fun getRemoteFactory(): Boolean
}