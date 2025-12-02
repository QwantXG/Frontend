package com.example.core_data.model

import kotlinx.serialization.Serializable

@Serializable
data class ExpenseRequestDTO(
    val name: String,
    val description: String? = null,
    val payerId: Long,
    val date: String,
    val participantShares: Map<Long, Double>,
    val categoryId: Long
)

@Serializable
data class ExpenseUpdateRequestDTO(
    val name: String? = null,
    val description: String? = null,
    val date: String? = null,
    val categoryId: Long? = null,
    val payerId: Long? = null,
    val participantShares: Map<Long, Double>? = null
)

@Serializable
data class MemberExpenseResponseDTO(
    val userId: Long,
    val name: String? = null,
    val surname: String? = null,
    val share: Double? = null
)

@Serializable
data class ExpenseResponseDTO(
    val id: Long,
    val payerId: Long,
    val name: String,
    val description: String? = null,
    val sum: Double? = null,
    val date: String,
    val categoryId: Long? = null,
    val categoryName: String? = null,
    val participants: List<MemberExpenseResponseDTO> = emptyList()
)

@Serializable
data class TravelExpensesResponseDTO(
    val totalAmount: Double? = null,
    val totalCount: Int? = null,
    val expenses: List<ExpenseResponseDTO> = emptyList()
)
