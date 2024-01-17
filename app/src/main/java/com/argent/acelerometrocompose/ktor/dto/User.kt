package com.argent.acelerometrocompose.ktor.dto

import kotlinx.serialization.Serializable


@Serializable
data class UserRequest(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val rol: String? = null,
)

@Serializable
data class UserResponse(
    val data: UserInfo,
    val message: String
)


@Serializable
data class UserInfo(
    val id: String,
    val name: String,
    val email: String,
)



