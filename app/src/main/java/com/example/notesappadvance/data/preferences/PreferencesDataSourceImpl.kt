package com.example.notesappadvance.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.notesappadvance.utils.Constants.TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataSourceImpl @Inject constructor(private val dataStore: DataStore<Preferences>) : PreferencesDataSource {
    override suspend fun setToken(token: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(TOKEN)] = token
        }
    }

    override suspend fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(TOKEN)] ?: ""
        }
    }
}