package com.example.domain.repo

import kotlinx.coroutines.flow.Flow

interface DataStoreRepo {

    val dataStoreFlow: Flow<String>

    suspend fun setTestData(text: String)
}