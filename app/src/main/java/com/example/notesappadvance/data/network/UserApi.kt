package com.example.notesappadvance.data.network

import com.example.notesappadvance.models.registation.RegistationResponse
import com.example.notesappadvance.models.login.LoginRequest
import com.example.notesappadvance.models.login.LoginResponse
import com.example.notesappadvance.models.registation.RegistationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("/api/v1/auth/login")
    suspend fun requestLogin(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/api/v1/users")
    suspend fun requestSignup(@Body registationRequest: RegistationRequest): Response<RegistationResponse>
}