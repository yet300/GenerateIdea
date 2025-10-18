package com.yet.generate.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatsResponse(
    @SerialName("total_ideas")
    val totalIdeas: Int,
    @SerialName("banal_ideas")
    val banalIdeas: Int,
    @SerialName("unique_ideas")
    val uniqueIdeas: Int,
    @SerialName("by_type")
    val byType: Map<String, Int>
)
