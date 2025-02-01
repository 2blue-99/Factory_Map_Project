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
    private val dataStore: DataStore<Preferences>
): UserDataStore {

    object PreferencesKey {
        val DOWNLOAD = booleanPreferencesKey("DOWNLOAD")
        val SELECTED_AREA = intPreferencesKey("AREA")
        val CLUSTER_TRIGGER_TYPE_POSITION = intPreferencesKey("CLUSTER_TRIGGER_TYPE_POSITION")
    }

    override val downloadFlow: Flow<Boolean> =
        dataStore.data.map { dataStore -> dataStore[PreferencesKey.DOWNLOAD] ?: false }

    override val areaPositionFlow: Flow<Int> =
        dataStore.data.map { dataStore -> dataStore[PreferencesKey.SELECTED_AREA] ?: 0 }

    // 디폴트 값은 3
    override val clusterTriggerTypePositionFlow: Flow<Int> =
        dataStore.data.map { dataStore -> dataStore[PreferencesKey.CLUSTER_TRIGGER_TYPE_POSITION] ?: 2 }


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
}