package com.example.domain.repo

import com.example.domain.model.FactoryInfo

interface FireStoreRepository {
    fun login()

    suspend fun insertRemoteFactory(factoryInfo: FactoryInfo)

    suspend fun getRemoteFactory(): Boolean
}