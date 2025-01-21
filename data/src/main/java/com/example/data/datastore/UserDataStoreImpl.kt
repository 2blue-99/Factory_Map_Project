package com.example.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
        val DOWNLOAD = stringPreferencesKey("DOWNLOAD")
        val SELECTED_AREA = intPreferencesKey("AREA")
    }

    override val downloadFlow: Flow<String> =
        dataStore.data.map { dataStore -> dataStore[PreferencesKey.DOWNLOAD] ?: "1999-08-31" }

    override val areaPositionFlow: Flow<Int> =
        dataStore.data.map { dataStore -> dataStore[PreferencesKey.SELECTED_AREA] ?: 0 }


    override suspend fun setDownload(text: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.DOWNLOAD] = text
        }
    }

    override suspend fun setArea(position: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.SELECTED_AREA] = position
        }
    }
}