package com.yet.generate.domain.model

data class GenerateIdeaResponse(
    val idea: String,
    val type: String,
    val complexity: Complexity,
    val tags: List<String>,
    val timeEstimate: String,
    val unique: Boolean,
    val similarityScore: Double,
    val details: String
)
