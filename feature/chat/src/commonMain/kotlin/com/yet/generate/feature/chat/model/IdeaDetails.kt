package com.yet.generate.feature.chat.model

import com.yet.generate.domain.model.Complexity

data class IdeaDetails(
    val type: String,
    val complexity: Complexity,
    val tags: List<String>,
    val timeEstimate: String,
    val unique: Boolean,
    val similarityScore: Double
)