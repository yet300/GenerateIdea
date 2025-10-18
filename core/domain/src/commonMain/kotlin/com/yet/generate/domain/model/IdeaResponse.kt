package com.yet.generate.domain.model

data class IdeaResponse(
    val id: Int,
    val idea: String,
    val type: String,
    val complexity: Complexity,
    val tags: List<String>,
    val timeEstimate: String?,
    val createdAt: String,
    val isBanal: Boolean
)
