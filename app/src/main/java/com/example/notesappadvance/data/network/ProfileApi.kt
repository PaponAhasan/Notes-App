package com.example.notesappadvance.data.network

import com.example.notesappadvance.models.profile.AllUserResponse
import com.example.notesappadvance.models.profile.UpdateUserRequest
import com.example.notesappadvance.models.profile.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileApi {

    @GET("/api/v1/users")
    suspend fun getAllProfile(): Response<AllUserResponse>

    @GET("/api/v1/users/{id}")
    suspend fun getSingleProfile(@Path("id") id: Int): Response<UserResponse>

    @PUT("/api/v1/users/{id}")
    suspend fun updateProfile(
        @Path("id") id: Int,
        @Body updateUserRequest: UpdateUserRequest
    ): Response<UserResponse>
}