package com.yet.generate.api.model

import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    val status: String,
    val message: String,
    val version: String
)
