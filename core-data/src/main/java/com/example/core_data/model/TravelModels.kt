package com.example.core_data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateTravelRequest(
    val name: String,
    val description: String? = null,
    val startDate: String,
    val endDate: String? = null
)

@Serializable
data class EditTravelRequest(
    val name: String? = null,
    val description: String? = null,
    val startDate: String? = null,
    val endDate: String? = null
)

@Serializable
data class TravelResponse(
    val id: Long,
    val name: String,
    val description: String? = null,
    val startDate: String,
    val endDate: String? = null,
    val status: String? = null
)

@Serializable
data class MyTravelItem(
    val id: Long,
    val name: String,
    val description: String? = null,
    val startDate: String,
    val endDate: String? = null,
    val travelStatus: String? = null
)

@Serializable
data class MyTravelsResponse(
    val travels: List<MyTravelItem> = emptyList()
)

@Serializable
data class TravelExpenseAnalyticsDTO(
    val totalAmount: Double? = null,
    val categories: List<CategoryAnalyticsResponseDTO> = emptyList()
)
