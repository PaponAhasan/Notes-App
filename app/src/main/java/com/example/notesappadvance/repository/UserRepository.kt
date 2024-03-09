package com.example.notesappadvance.repository

import com.example.notesappadvance.data.network.RemoteDataSource
import com.example.notesappadvance.data.preferences.PreferencesDataSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    preferencesDataSource: PreferencesDataSource
) {
    val remote = remoteDataSource
    val dataStore = preferencesDataSource
}