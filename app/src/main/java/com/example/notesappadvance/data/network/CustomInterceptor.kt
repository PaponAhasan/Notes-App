package com.example.notesappadvance.data.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class CustomInterceptor @Inject constructor(private val token: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Accept", "application/json")
                .build()
            return chain.proceed(request) // Proceed with the request

        } catch (e: Exception) {
            throw e
        }
    }
}