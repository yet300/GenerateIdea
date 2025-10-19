package com.yet.generate.feature.chat.model

import com.yet.generate.domain.model.Complexity

enum class ComplexityLevel(
    val displayName: String,
    val mental: String,
    val implementation: String,
    val teamSize: String
) {
    LOW(
        displayName = "Low",
        mental = "low",
        implementation = "low",
        teamSize = "1-2"
    ),
    MEDIUM(
        displayName = "Medium",
        mental = "medium",
        implementation = "medium",
        teamSize = "3-5"
    ),
    HIGH(
        displayName = "High",
        mental = "high",
        implementation = "high",
        teamSize = "5+"
    );

    fun toComplexity(): Complexity = Complexity(
        mental = mental,
        implementation = implementation,
        teamSize = teamSize
    )
}