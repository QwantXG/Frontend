package com.example.core_data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateTransferRequest(
    val senderId: Long,
    val recipientId: Long,
    val sum: Double
)

@Serializable
data class EditTransferRequest(
    val sum: Double
)

@Serializable
data class TransferResponse(
    val id: Long,
    val travelId: Long,
    val senderId: Long,
    val recipientId: Long,
    val sum: Double,
    val date: String? = null
)

@Serializable
data class TransfersListResponse(
    val transfers: List<TransferResponse> = emptyList()
)
