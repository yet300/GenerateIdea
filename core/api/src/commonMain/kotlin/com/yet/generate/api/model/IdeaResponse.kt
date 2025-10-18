package com.yet.generate.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IdeaResponse(
    val id: Int,
    val idea: String,
    val type: String,
    val complexity: Complexity,
    val tags: List<String>,
    @SerialName("time_estimate")
    val timeEstimate: String?,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("is_banal")
    val isBanal: Boolean
)
