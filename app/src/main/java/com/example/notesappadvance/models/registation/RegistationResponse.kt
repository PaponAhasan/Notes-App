package com.example.notesappadvance.models.registation

data class RegistationResponse(
    val avatar: String,
    val email: String,
    val id: Int,
    val name: String,
    val password: String,
    val role: String
)