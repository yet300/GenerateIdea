package com.yet.generate.data.repository

import com.yet.generate.api.IdeaGeneratorApi
import com.yet.generate.api.model.BatchGenerateRequest
import com.yet.generate.api.model.GenerateIdeaRequest
import com.yet.generate.api.model.RandomIdeaRequest
import com.yet.generate.data.mapper.toApi
import com.yet.generate.data.mapper.toDomain
import com.yet.generate.domain.model.*
import com.yet.generate.domain.repository.IdeaRepository
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Singleton
class IdeaRepositoryImpl(
    private val api: IdeaGeneratorApi
) : IdeaRepository {

    override suspend fun checkHealth(): Result<HealthResponse> = runCatching {
        api.healthCheck().toDomain()
    }

    override suspend fun generateIdea(
        niche: String,
        type: String,
        budget: Int?,
        complexityConstraints: Complexity?,
        timeEstimate: String?
    ): Result<GenerateIdeaResponse> = runCatching {
        api.generateIdea(
            GenerateIdeaRequest(
                niche = niche,
                type = type,
                budget = budget,
                complexityConstraints = complexityConstraints?.toApi(),
                timeEstimate = timeEstimate
            )
        ).toDomain()
    }

    override suspend fun generateBatch(
        niche: String,
        type: String,
        count: Int,
        budget: Int?,
        complexityConstraints: Complexity?,
        timeEstimate: String?
    ): Result<BatchGenerateResponse> = runCatching {
        api.generateBatch(
            BatchGenerateRequest(
                niche = niche,
                type = type,
                count = count,
                budget = budget,
                complexityConstraints = complexityConstraints?.toApi(),
                timeEstimate = timeEstimate
            )
        ).toDomain()
    }

    override suspend fun generateRandom(
        count: Int,
        filters: Map<String, String>?
    ): Result<BatchGenerateResponse> = runCatching {
        api.generateRandom(
            RandomIdeaRequest(
                count = count,
                filters = filters
            )
        ).toDomain()
    }

    override suspend fun getTags(): Result<TagsResponse> = runCatching {
        api.getTags().toDomain()
    }

    override suspend fun getIdeas(
        type: String?,
        minMental: String?,
        maxTeamSize: String?,
        includeBanal: Boolean,
        limit: Int
    ): Result<List<IdeaResponse>> = runCatching {
        api.getIdeas(
            type = type,
            minMental = minMental,
            maxTeamSize = maxTeamSize,
            includeBanal = includeBanal,
            limit = limit
        ).map { it.toDomain() }
    }

    override suspend fun getStats(): Result<StatsResponse> = runCatching {
        api.getStats().toDomain()
    }

    override fun generateIdeaFlow(
        niche: String,
        type: String,
        budget: Int?
    ): Flow<Result<GenerateIdeaResponse>> = flow {
        emit(generateIdea(niche, type, budget))
    }

    override fun generateBatchFlow(
        niche: String,
        type: String,
        count: Int
    ): Flow<Result<BatchGenerateResponse>> = flow {
        emit(generateBatch(niche, type, count))
    }
}
