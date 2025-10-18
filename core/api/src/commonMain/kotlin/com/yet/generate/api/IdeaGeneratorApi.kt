package com.yet.generate.api

import com.yet.generate.api.model.*
import de.jensklingenberg.ktorfit.http.*

interface IdeaGeneratorApi {

    @GET("/")
    suspend fun healthCheck(): HealthResponse

    @POST("/generate_idea")
    suspend fun generateIdea(@Body request: GenerateIdeaRequest): GenerateIdeaResponse

    @POST("/generate_batch")
    suspend fun generateBatch(@Body request: BatchGenerateRequest): BatchGenerateResponse

    @POST("/generate_random")
    suspend fun generateRandom(@Body request: RandomIdeaRequest): BatchGenerateResponse

    @GET("/tags")
    suspend fun getTags(): TagsResponse

    @GET("/ideas")
    suspend fun getIdeas(
        @Query("type") type: String? = null,
        @Query("min_mental") minMental: String? = null,
        @Query("max_team_size") maxTeamSize: String? = null,
        @Query("include_banal") includeBanal: Boolean = false,
        @Query("limit") limit: Int = 50
    ): List<IdeaResponse>

    @GET("/stats")
    suspend fun getStats(): StatsResponse
}
