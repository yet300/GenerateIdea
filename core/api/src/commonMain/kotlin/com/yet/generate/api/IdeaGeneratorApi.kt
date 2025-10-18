package com.yet.generate.api

import com.yet.generate.api.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class IdeaGeneratorApi(private val client: HttpClient) {

    suspend fun healthCheck(): HealthResponse {
        return client.get("/").body()
    }

    suspend fun generateIdea(request: GenerateIdeaRequest): GenerateIdeaResponse {
        return client.post("/generate_idea") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun generateBatch(request: BatchGenerateRequest): BatchGenerateResponse {
        return client.post("/generate_batch") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun generateRandom(request: RandomIdeaRequest): BatchGenerateResponse {
        return client.post("/generate_random") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun getTags(): TagsResponse {
        return client.get("/tags").body()
    }

    suspend fun getIdeas(
        type: String? = null,
        minMental: String? = null,
        maxTeamSize: String? = null,
        includeBanal: Boolean = false,
        limit: Int = 50
    ): List<IdeaResponse> {
        return client.get("/ideas") {
            parameter("type", type)
            parameter("min_mental", minMental)
            parameter("max_team_size", maxTeamSize)
            parameter("include_banal", includeBanal)
            parameter("limit", limit)
        }.body()
    }

    suspend fun getStats(): StatsResponse {
        return client.get("/stats").body()
    }
}
