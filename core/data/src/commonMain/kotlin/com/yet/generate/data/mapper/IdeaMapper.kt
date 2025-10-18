package com.yet.generate.data.mapper

import com.yet.generate.api.model.BatchGenerateResponse as ApiBatchGenerateResponse
import com.yet.generate.api.model.Complexity as ApiComplexity
import com.yet.generate.api.model.GenerateIdeaResponse as ApiGenerateIdeaResponse
import com.yet.generate.api.model.HealthResponse as ApiHealthResponse
import com.yet.generate.api.model.IdeaResponse as ApiIdeaResponse
import com.yet.generate.api.model.StatsResponse as ApiStatsResponse
import com.yet.generate.api.model.TagsResponse as ApiTagsResponse
import com.yet.generate.domain.model.BatchGenerateResponse
import com.yet.generate.domain.model.Complexity
import com.yet.generate.domain.model.GenerateIdeaResponse
import com.yet.generate.domain.model.HealthResponse
import com.yet.generate.domain.model.IdeaResponse
import com.yet.generate.domain.model.StatsResponse
import com.yet.generate.domain.model.TagsResponse

fun ApiComplexity.toDomain() = Complexity(
    mental = mental,
    implementation = implementation,
    teamSize = teamSize
)

fun Complexity.toApi() = ApiComplexity(
    mental = mental,
    implementation = implementation,
    teamSize = teamSize
)

fun ApiGenerateIdeaResponse.toDomain() = GenerateIdeaResponse(
    idea = idea,
    type = type,
    complexity = complexity.toDomain(),
    tags = tags,
    timeEstimate = timeEstimate,
    unique = unique,
    similarityScore = similarityScore,
    details = details
)

fun ApiBatchGenerateResponse.toDomain() = BatchGenerateResponse(
    ideas = ideas.map { it.toDomain() },
    totalGenerated = totalGenerated,
    uniqueCount = uniqueCount,
    duplicateCount = duplicateCount
)

fun ApiHealthResponse.toDomain() = HealthResponse(
    status = status,
    message = message,
    version = version
)

fun ApiTagsResponse.toDomain() = TagsResponse(
    types = types,
    complexityLevels = complexityLevels,
    timeEstimates = timeEstimates,
    allTags = allTags
)

fun ApiIdeaResponse.toDomain() = IdeaResponse(
    id = id,
    idea = idea,
    type = type,
    complexity = complexity.toDomain(),
    tags = tags,
    timeEstimate = timeEstimate,
    createdAt = createdAt,
    isBanal = isBanal
)

fun ApiStatsResponse.toDomain() = StatsResponse(
    totalIdeas = totalIdeas,
    banalIdeas = banalIdeas,
    uniqueIdeas = uniqueIdeas,
    byType = byType
)
