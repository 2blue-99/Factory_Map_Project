package com.example.domain.repo

import kotlinx.coroutines.flow.Flow

interface DataStoreRepo {

    val downloadFlow: Flow<String>
    val areaPositionFlow: Flow<Int>

    suspend fun setDownload(text: String)
    suspend fun setArea(position: Int)
}