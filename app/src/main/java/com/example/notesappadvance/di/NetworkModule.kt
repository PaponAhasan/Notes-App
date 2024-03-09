package com.example.notesappadvance.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.notesappadvance.data.network.CustomInterceptor
import com.example.notesappadvance.data.network.RemoteDataSource
import com.example.notesappadvance.data.network.RemoteDataSourceImpl
import com.example.notesappadvance.data.network.UserApi
import com.example.notesappadvance.utils.Constants
import com.example.notesappadvance.utils.Constants.BASE_URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    suspend fun provideToken(dataStore: DataStore<Preferences>): String {
        return withContext(Dispatchers.IO) {
            dataStore.data.first()[stringPreferencesKey(Constants.TOKEN)] ?: ""
        }
    }

    @Provides
    @Singleton
    fun provideInterceptor(token: String): CustomInterceptor {
        return CustomInterceptor(token)
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(customInterceptor: CustomInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(customInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            //.addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(userApi: UserApi): RemoteDataSource {
        return RemoteDataSourceImpl(userApi)
    }
}