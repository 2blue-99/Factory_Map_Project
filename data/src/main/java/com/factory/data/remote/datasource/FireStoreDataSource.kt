package com.factory.data.remote.datasource

import com.factory.data.remote.model.FactoryResponse

interface FireStoreDataSource {
    suspend fun login(id: String, passWord: String): String

    suspend fun getAllData(userCode: String): List<Pair<String,FactoryResponse>>

    suspend fun insertData(dataList: List<FactoryResponse>): Boolean
}