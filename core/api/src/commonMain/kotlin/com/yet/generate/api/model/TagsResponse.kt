package com.yet.generate.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagsResponse(
    val types: List<String>,
    @SerialName("complexity_levels")
    val complexityLevels: List<String>,
    @SerialName("time_estimates")
    val timeEstimates: List<String>,
    @SerialName("all_tags")
    val allTags: List<String>
)
