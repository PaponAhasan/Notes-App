package com.example.notesappadvance.data.preferences

import kotlinx.coroutines.flow.Flow

interface PreferencesDataSource {
    suspend fun setToken(token: String)
    suspend fun getToken(): Flow<String>
}