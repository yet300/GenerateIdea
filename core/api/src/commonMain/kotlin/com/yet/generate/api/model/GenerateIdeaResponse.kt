package com.yet.generate.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerateIdeaResponse(
    val idea: String,
    val type: String,
    val complexity: Complexity,
    val tags: List<String>,
    @SerialName("time_estimate")
    val timeEstimate: String,
    val unique: Boolean,
    @SerialName("similarity_score")
    val similarityScore: Double,
    val details: String
)
