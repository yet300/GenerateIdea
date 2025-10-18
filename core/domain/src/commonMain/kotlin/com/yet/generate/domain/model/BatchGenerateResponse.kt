package com.yet.generate.domain.model

data class BatchGenerateResponse(
    val ideas: List<GenerateIdeaResponse>,
    val totalGenerated: Int,
    val uniqueCount: Int,
    val duplicateCount: Int
)
