package com.yet.generate.domain.model

data class StatsResponse(
    val totalIdeas: Int,
    val banalIdeas: Int,
    val uniqueIdeas: Int,
    val byType: Map<String, Int>
)
