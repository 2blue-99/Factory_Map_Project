package com.example.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
): DataStoreRepo {

    object PreferencesKey {
        val TEST = stringPreferencesKey("TEST")
    }

    override val dataStoreFlow: Flow<String> =
        dataStore.data.map { dataStore -> dataStore[PreferencesKey.TEST] ?: "0" }


    override suspend fun setTestData(text: String) {
        Timber.d("setTestData : $text")
        dataStore.edit { preferences ->
            preferences[PreferencesKey.TEST] = text
        }
    }
}