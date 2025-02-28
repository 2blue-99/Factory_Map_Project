package com.example.data.remote.datasource

import com.example.data.remote.model.FactoryResponse
import kotlinx.coroutines.flow.Flow

interface FirebaseDataSource {
    fun getAllData(): Flow<List<FactoryResponse>>
    fun insertData(): Boolean
}