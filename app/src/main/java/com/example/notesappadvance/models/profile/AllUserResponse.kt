package com.example.notesappadvance.models.profile

data class AllUserResponse(
    val users: List<UserResponse>
)

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String,
    val password: String,
    val role: String
)