package com.example.core_data.network

import com.example.core_data.model.AuthResponse

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val accessExpiresAtMillis: Long? = null,
    val refreshExpiresAtMillis: Long? = null
) {
    fun isAccessExpired(nowMillis: Long = System.currentTimeMillis()): Boolean =
        accessExpiresAtMillis?.let { nowMillis >= it } ?: false

    fun shouldRefreshAccess(thresholdMillis: Long = NetworkDefaults.ACCESS_REFRESH_THRESHOLD_MS, nowMillis: Long = System.currentTimeMillis()): Boolean =
        accessExpiresAtMillis?.let { nowMillis >= it - thresholdMillis } ?: false

    fun isRefreshExpired(nowMillis: Long = System.currentTimeMillis()): Boolean =
        refreshExpiresAtMillis?.let { nowMillis >= it } ?: false
}

fun AuthResponse.toAuthTokens(nowMillis: Long = System.currentTimeMillis()): AuthTokens? {
    val access = accessToken
    val refresh = refreshToken
    if (access.isNullOrBlank() || refresh.isNullOrBlank()) return null

    val accessExpiresAt = accessTokenExpiresIn?.let { nowMillis + it * 1000 }
    val refreshExpiresAt = refreshTokenExpiresIn?.let { nowMillis + it * 1000 }

    return AuthTokens(
        accessToken = access,
        refreshToken = refresh,
        accessExpiresAtMillis = accessExpiresAt,
        refreshExpiresAtMillis = refreshExpiresAt
    )
}
