package com.example.notesappadvance.models.registation

data class RegistationRequest(
    val name: String,
    val email: String,
    val password: String,
    val avatar: String
)