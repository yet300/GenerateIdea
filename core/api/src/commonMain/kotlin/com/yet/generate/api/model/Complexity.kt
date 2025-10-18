package com.yet.generate.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Complexity(
    val mental: String,
    val implementation: String,
    @SerialName("team_size")
    val teamSize: String
)
