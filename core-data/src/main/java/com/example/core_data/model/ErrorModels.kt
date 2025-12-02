package com.example.core_data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: Int? = null,
    val error: String? = null,
    val message: String? = null,
    val timestamp: String? = null
)
