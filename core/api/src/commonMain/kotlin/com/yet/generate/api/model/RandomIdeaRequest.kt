package com.yet.generate.api.model

import kotlinx.serialization.Serializable

@Serializable
data class RandomIdeaRequest(
    val count: Int = 1,
    val filters: Map<String, String>? = null
)
