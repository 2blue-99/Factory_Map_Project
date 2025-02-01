package com.example.data.datastore

import kotlinx.coroutines.flow.Flow

interface UserDataStore {

    val downloadFlow: Flow<Boolean>
    val areaPositionFlow: Flow<Int>
    val clusterTriggerTypePositionFlow: Flow<Int>

    suspend fun setDownload(state: Boolean)
    suspend fun setArea(position: Int)
    suspend fun setClusterTriggerTypePosition(position: Int)
}