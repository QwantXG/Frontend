package com.example.core_data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateCategoryRequest(
    val name: String
)

@Serializable
data class EditCategoryRequest(
    val name: String
)

@Serializable
data class CategoryResponse(
    val id: Long,
    val travelId: Long,
    val name: String
)

@Serializable
data class CategoriesListResponse(
    val items: List<CategoryResponse> = emptyList()
)

@Serializable
data class ParticipantStats(
    val name: String? = null,
    val surname: String? = null,
    val expenseAmount: Double? = null
)

@Serializable
data class CategoryAnalyticsResponseDTO(
    val id: Long,
    val name: String,
    val totalAmount: Double? = null,
    val percentageOfTotal: Double? = null,
    val expenseCount: Int? = null,
    val participants: List<ParticipantStats> = emptyList()
)
