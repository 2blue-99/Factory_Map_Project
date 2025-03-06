package com.example.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
): UserDataStore {

    object PreferencesKey {
        val DOWNLOAD = booleanPreferencesKey("DOWNLOAD")
        val SELECTED_AREA = intPreferencesKey("AREA")
        val CLUSTER_TRIGGER_TYPE_POSITION = intPreferencesKey("CLUSTER_TRIGGER_TYPE_POSITION")
        val CURRENT_POSITION = stringPreferencesKey("CURRENT_POSITION")
        val CONNECTED_STATE = booleanPreferencesKey("CONNECTED_STATE")
        val USER_CODE = stringPreferencesKey("USER_CODE")
    }

    override val downloadFlow: Flow<Boolean> =
        dataStore.data.map { dataStore -> dataStore[PreferencesKey.DOWNLOAD] ?: false }

    override val areaPositionFlow: Flow<Int> =
        dataStore.data.map { dataStore -> dataStore[PreferencesKey.SELECTED_AREA] ?: 0 }

    // 디폴트 값은 3
    override val clusterTriggerTypePositionFlow: Flow<Int> =
        dataStore.data.map { dataStore -> dataStore[PreferencesKey.CLUSTER_TRIGGER_TYPE_POSITION] ?: 2 }

    override val currentLocationFlow: Flow<Triple<Double, Double, Double>>
        get() {
            return dataStore.data.map { dataStore -> dataStore[PreferencesKey.CURRENT_POSITION] ?: "36.658,127.8766,0.1" }.map {
                val data = it.split(",")
                Triple(data[0].toDouble(), data[1].toDouble(), data[2].toDouble())
            }
        }




    override val connectedStateFlow: Flow<Boolean> =
        dataStore.data.map { dataStore -> dataStore[PreferencesKey.CONNECTED_STATE] ?:true }

    override val userCodeFlow: Flow<String> =
        dataStore.data.map { dataStore -> dataStore[PreferencesKey.USER_CODE] ?: "1234" }


    override suspend fun setDownload(state: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.DOWNLOAD] = state
        }
    }

    override suspend fun setArea(position: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.SELECTED_AREA] = position
        }
    }

    override suspend fun setClusterTriggerTypePosition(position: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.CLUSTER_TRIGGER_TYPE_POSITION] = position
        }
    }

    override suspend fun setCurrentLocation(location: Triple<Double, Double, Double>) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.CURRENT_POSITION] = "${location.first},${location.second},${location.third}"
        }
    }

    override suspend fun setConnectedState(state: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.CONNECTED_STATE] = state
        }
    }

    override suspend fun setUserCode(state: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.USER_CODE] = state
        }
    }
}