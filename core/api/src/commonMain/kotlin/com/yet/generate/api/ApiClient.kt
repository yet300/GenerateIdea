package com.yet.generate.api

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ApiClientBuilder(
    private val baseUrl: String = BuildKonfig.API_BASE_URL,
    private val enableLogging: Boolean = true,
    private val requestTimeout: Long = 60_000,
    private val connectTimeout: Long = 30_000
) {
    fun build(): IdeaGeneratorApi {
        val httpClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }

            if (enableLogging) {
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.INFO
                }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = requestTimeout
                connectTimeoutMillis = connectTimeout
                socketTimeoutMillis = requestTimeout
            }

            defaultRequest {
                url(baseUrl)
            }
        }

        return IdeaGeneratorApi(httpClient)
    }
}

object ApiClient {
    val api: IdeaGeneratorApi by lazy {
        ApiClientBuilder(
            baseUrl = BuildKonfig.API_BASE_URL,
            enableLogging = true
        ).build()
    }
}
