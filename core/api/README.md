# API Module

Complete Kotlin Multiplatform API module for the Idea Generator service using Ktor Client.

## Structure

```
core/api/
├── src/commonMain/kotlin/com/yet/generate/api/
│   ├── IdeaGeneratorApi.kt          # Ktorfit API interface
│   ├── ApiClient.kt                  # HTTP client configuration
│   ├── di/
│   │   └── ApiModule.kt              # Koin DI module
│   ├── models/
│   │   ├── Complexity.kt
│   │   ├── GenerateIdeaRequest.kt
│   │   ├── GenerateIdeaResponse.kt
│   │   ├── BatchGenerateRequest.kt
│   │   ├── BatchGenerateResponse.kt
│   │   ├── RandomIdeaRequest.kt
│   │   ├── IdeaResponse.kt
│   │   ├── TagsResponse.kt
│   │   ├── StatsResponse.kt
│   │   └── HealthResponse.kt
│   └── repository/
│       └── IdeaRepository.kt         # Repository with error handling
```

## Usage

### Setup Koin Module

```kotlin
startKoin {
    modules(ApiModule().module)
}
```

### Inject Repository

```kotlin
class MyViewModel(
    private val repository: IdeaRepository
) {
    suspend fun loadIdea() {
        repository.generateIdea(
            niche = "AI in healthcare",
            type = "startup"
        ).onSuccess { idea ->
            println("Generated: ${idea.idea}")
        }
    }
}
```

### Direct API Usage

```kotlin
val api = ApiClient.api

// Health check
val health = api.healthCheck()
println("Status: ${health.status}")

// Generate idea
val response = api.generateIdea(
    GenerateIdeaRequest(
        niche = "blockchain gaming",
        type = "hackathon"
    )
)
```

## Configuration

### Setup Secret Properties

1. Copy `secret.properties.example` to `secret.properties`:
   ```bash
   cp secret.properties.example secret.properties
   ```

2. Update `secret.properties` with your API URL:
   ```properties
   API_BASE_URL=http://127.0.0.1:8001
   ```

3. The file is automatically ignored by git for security.

### BuildKonfig

The API base URL is managed via BuildKonfig and loaded from `secret.properties`. The generated `BuildKonfig` object is available at compile time:

```kotlin
// Access the base URL
val baseUrl = BuildKonfig.API_BASE_URL
```

### Custom Configuration

Override settings when building the API client:

```kotlin
val api = ApiClientBuilder(
    baseUrl = "https://your-api.com",
    enableLogging = false,  // Disable in production
    requestTimeout = 30_000
).build()
```

## Dependencies

- `ktor-client-core` - Core Ktor client
- `ktor-client-content-negotiation` - JSON content negotiation
- `ktor-serialization-kotlinx-json` - Kotlinx serialization
- `ktor-client-logging` - Request/response logging
- Platform-specific engines (Android, iOS, Desktop)
