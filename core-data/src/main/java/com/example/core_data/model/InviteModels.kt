package com.example.core_data.model

import kotlinx.serialization.Serializable

@Serializable
data class InviteRequest(
    val phones: List<String>
)

@Serializable
data class InvitesItem(
    val inviteId: Long,
    val travelName: String? = null,
    val description: String? = null,
    val startDate: String? = null,
    val endDate: String? = null
)

@Serializable
data class InvitesResponse(
    val invites: List<InvitesItem> = emptyList()
)
