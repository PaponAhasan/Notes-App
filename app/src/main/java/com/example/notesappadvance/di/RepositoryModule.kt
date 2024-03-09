package com.example.notesappadvance.di

import com.example.notesappadvance.data.network.RemoteDataSourceImpl
import com.example.notesappadvance.data.preferences.PreferencesDataSourceImpl
import com.example.notesappadvance.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//if have multiple data source in Repository
@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideUserRepository(remoteDataSourceImpl: RemoteDataSourceImpl, preferencesDataSourceImpl: PreferencesDataSourceImpl): UserRepository {
        return UserRepository(remoteDataSourceImpl, preferencesDataSourceImpl)
    }
}