package com.yet.generate.feature.chat.model

data class IdeaFilters(
    val niche: String = "",
    val type: String = "startup",
    val budget: Int? = null,
    val timeEstimate: String? = null
)