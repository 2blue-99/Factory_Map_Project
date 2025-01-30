package com.example.data.datastore

import kotlinx.coroutines.flow.Flow

interface UserDataStore {

    val downloadFlow: Flow<String>
    val areaPositionFlow: Flow<Int>
    val clusterTriggerTypePositionFlow: Flow<Int>

    suspend fun setDownload(text: String)
    suspend fun setArea(position: Int)
    suspend fun setClusterTriggerTypePosition(position: Int)
}