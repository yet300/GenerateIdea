package com.yet.generate.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BatchGenerateResponse(
    val ideas: List<GenerateIdeaResponse>,
    @SerialName("total_generated")
    val totalGenerated: Int,
    @SerialName("unique_count")
    val uniqueCount: Int,
    @SerialName("duplicate_count")
    val duplicateCount: Int
)
