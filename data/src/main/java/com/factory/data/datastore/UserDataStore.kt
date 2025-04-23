package com.factory.data.datastore

import kotlinx.coroutines.flow.Flow

interface UserDataStore {

    val downloadFlow: Flow<Boolean>
    val areaPositionFlow: Flow<Int>
    val clusterTriggerTypePositionFlow: Flow<Int>
    val currentLocationFlow: Flow<Triple<Double, Double, Double>> // 위도, 경도, Zoom
    val connectedStateFlow: Flow<Boolean> // 위도, 경도, Zoom
    val searchNameFlow: Flow<String> // 위도, 경도, Zoom

    val userCodeFlow: Flow<String>
    val autoLoginFlow: Flow<Boolean>
    val isInitFlow: Flow<Boolean>

    suspend fun setDownload(state: Boolean)
    suspend fun setArea(position: Int)
    suspend fun setClusterTriggerTypePosition(position: Int)
    suspend fun setCurrentLocation(location: Triple<Double, Double, Double>)
    suspend fun setConnectedState(state: Boolean)
    suspend fun setSearchName(name: String)

    suspend fun setUserCode(state: String)
    suspend fun setAutoLogin(state: Boolean)
    suspend fun setInit(state: Boolean)
}