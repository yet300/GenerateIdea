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

2. Update `secret.properties` with your API URL based on your target device:

   **For iOS Simulator / Desktop:**
   ```properties
   API_BASE_URL=http://127.0.0.1:8001
   ```

   **For Android Emulator:**
   ```properties
   API_BASE_URL=http://10.0.2.2:8001
   ```
   (10.0.2.2 is a special IP that routes to the host machine)

   **For Physical Devices:**
   ```properties
   API_BASE_URL=http://192.168.1.XXX:8001
   ```
   Replace `192.168.1.XXX` with your computer's local network IP address.

   **Finding your local IP:**
   - **macOS**: System Settings > Network > Wi-Fi > Details > TCP/IP
   - **Windows**: Run `ipconfig` in Command Prompt (look for IPv4 Address)
   - **Linux**: Run `ip addr show` or `ifconfig`

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
