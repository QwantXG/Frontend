package com.example.core_data.model

import kotlinx.serialization.Serializable

@Serializable
data class TravelMemberItem(
    val id: Long,
    val name: String? = null,
    val phone: String? = null,
    val status: String? = null
)

@Serializable
data class TravelMembersResponse(
    val members: List<TravelMemberItem> = emptyList()
)
