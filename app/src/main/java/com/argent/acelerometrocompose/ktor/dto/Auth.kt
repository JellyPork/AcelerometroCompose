package com.argent.acelerometrocompose.ktor.dto

import kotlinx.serialization.Serializable


@Serializable
data class AuthRequest(
    val email: String,
    val password: String
)
@Serializable
data class UserAnswer(
    val id: String,
    val name: String,
    val email: String,
)

@Serializable
data class AuthResponse(
    val message: String? = null,
    val user: UserAnswer? = null
)

@Serializable
data class CodeResponse(
    val message: String? = null,
    val data: String
)

