package com.example.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
): DataStoreRepo {

    object PreferencesKey {
        val DOWNLOAD = stringPreferencesKey("DOWNLOAD")
    }

    override val downloadFlow: Flow<String> =
        dataStore.data.map { dataStore -> dataStore[PreferencesKey.DOWNLOAD] ?: "1999-08-31" }


    override suspend fun setDownload(text: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.DOWNLOAD] = text
        }
    }
}