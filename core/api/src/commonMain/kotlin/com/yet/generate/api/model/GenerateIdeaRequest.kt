package com.yet.generate.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerateIdeaRequest(
    val niche: String,
    val type: String,
    val budget: Int? = null,
    @SerialName("complexity_constraints")
    val complexityConstraints: Complexity? = null,
    @SerialName("time_estimate")
    val timeEstimate: String? = null
)
