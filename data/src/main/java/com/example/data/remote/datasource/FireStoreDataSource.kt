package com.example.data.remote.datasource

import com.example.data.remote.model.FactoryResponse

interface FireStoreDataSource {
    suspend fun getAllData(): List<Pair<String,FactoryResponse>>
    suspend fun insertData(dataList: List<FactoryResponse>): Boolean
}