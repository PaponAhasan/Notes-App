package com.example.notesappadvance.data.network

import com.example.notesappadvance.models.login.LoginRequest
import com.example.notesappadvance.models.login.LoginResponse
import com.example.notesappadvance.models.registation.RegistationRequest
import com.example.notesappadvance.models.registation.RegistationResponse
import retrofit2.Response

interface RemoteDataSource {

    suspend fun requestLogin(loginRequest: LoginRequest) : Response<LoginResponse>

    suspend fun requestSignup(registationRequest: RegistationRequest): Response<RegistationResponse>
}