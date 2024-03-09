package com.example.notesappadvance.data.network

import com.example.notesappadvance.models.login.LoginRequest
import com.example.notesappadvance.models.login.LoginResponse
import com.example.notesappadvance.models.registation.RegistationRequest
import com.example.notesappadvance.models.registation.RegistationResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val userApi: UserApi) : RemoteDataSource {
    override suspend fun requestLogin(loginRequest: LoginRequest): Response<LoginResponse> {
        return userApi.requestLogin(loginRequest)
    }

    override suspend fun requestSignup(registationRequest: RegistationRequest): Response<RegistationResponse> {
        return userApi.requestSignup(registationRequest)
    }
}