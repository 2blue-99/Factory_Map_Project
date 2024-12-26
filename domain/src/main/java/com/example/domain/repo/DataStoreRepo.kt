package com.example.domain.repo

import kotlinx.coroutines.flow.Flow

interface DataStoreRepo {

    val downloadFlow: Flow<String>

    suspend fun setDownload(text: String)
}