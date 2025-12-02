package com.example.core_data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRegisterRequest(
    val phone: String,
    val name: String,
    val surname: String,
    val password: String
)

@Serializable
data class AuthLoginRequest(
    val phone: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val accessToken: String,
    val accessTokenExpiresIn: Long? = null,
    val refreshToken: String,
    val refreshTokenExpiresIn: Long? = null
)

@Serializable
data class RefreshRequest(
    val refreshToken: String
)

@Serializable
data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)

@Serializable
data class LogoutRequest(
    val refreshToken: String
)

@Serializable
data class AccountResponse(
    val phone: String? = null,
    val name: String? = null,
    val surname: String? = null
)
