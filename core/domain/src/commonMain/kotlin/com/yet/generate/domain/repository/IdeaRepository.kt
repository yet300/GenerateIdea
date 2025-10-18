package com.yet.generate.domain.repository

import com.yet.generate.domain.model.*
import kotlinx.coroutines.flow.Flow

interface IdeaRepository {
    
    suspend fun checkHealth(): Result<HealthResponse>
    
    suspend fun generateIdea(
        niche: String,
        type: String,
        budget: Int? = null,
        complexityConstraints: Complexity? = null,
        timeEstimate: String? = null
    ): Result<GenerateIdeaResponse>
    
    suspend fun generateBatch(
        niche: String,
        type: String,
        count: Int = 3,
        budget: Int? = null,
        complexityConstraints: Complexity? = null,
        timeEstimate: String? = null
    ): Result<BatchGenerateResponse>
    
    suspend fun generateRandom(
        count: Int = 1,
        filters: Map<String, String>? = null
    ): Result<BatchGenerateResponse>
    
    suspend fun getTags(): Result<TagsResponse>
    
    suspend fun getIdeas(
        type: String? = null,
        minMental: String? = null,
        maxTeamSize: String? = null,
        includeBanal: Boolean = false,
        limit: Int = 50
    ): Result<List<IdeaResponse>>
    
    suspend fun getStats(): Result<StatsResponse>
    
    fun generateIdeaFlow(
        niche: String,
        type: String,
        budget: Int? = null
    ): Flow<Result<GenerateIdeaResponse>>
    
    fun generateBatchFlow(
        niche: String,
        type: String,
        count: Int = 3
    ): Flow<Result<BatchGenerateResponse>>
}
