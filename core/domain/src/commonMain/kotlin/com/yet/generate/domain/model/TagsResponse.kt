package com.yet.generate.domain.model

data class TagsResponse(
    val types: List<String>,
    val complexityLevels: List<String>,
    val timeEstimates: List<String>,
    val allTags: List<String>
)
