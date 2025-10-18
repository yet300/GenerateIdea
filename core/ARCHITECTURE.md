# Clean Architecture - Core Modules

This project follows **Clean Architecture** principles with clear separation of concerns across three layers.

## Module Structure

```
core/
├── domain/          # Business Logic Layer (Interfaces & Models)
├── data/            # Data Layer (Repository Implementations)
└── api/             # Network Layer (API Client & DTOs)
```

## Dependency Flow

```
┌─────────────────────────────────────────────────┐
│                  Presentation                    │
│              (UI, ViewModels, etc.)              │
└────────────────────┬────────────────────────────┘
                     │ depends on
                     ↓
┌─────────────────────────────────────────────────┐
│                   DOMAIN                         │
│  • Business Models (pure Kotlin)                 │
│  • Repository Interfaces                         │
│  • Use Cases (optional)                          │
│                                                   │
│  Dependencies: NONE (only Kotlin stdlib)         │
└────────────────────┬────────────────────────────┘
                     ↑ implements
                     │
┌─────────────────────────────────────────────────┐
│                    DATA                          │
│  • Repository Implementations                    │
│  • Mappers (API ↔ Domain)                       │
│  • Data Source Coordination                      │
│                                                   │
│  Dependencies: domain, api                       │
└────────────────────┬────────────────────────────┘
                     │ uses
                     ↓
┌─────────────────────────────────────────────────┐
│                     API                          │
│  • Ktor Client Configuration                     │
│  • API Interface (Ktorfit)                       │
│  • DTOs (with @Serializable)                     │
│  • BuildKonfig (configuration)                   │
│                                                   │
│  Dependencies: ktor, ktorfit, kotlinx-serialization │
└─────────────────────────────────────────────────┘
```

## Layer Responsibilities

### 1. Domain Layer (`core:domain`)

**Purpose**: Contains business logic and defines contracts

**Contains**:
- Pure Kotlin data classes (domain models)
- Repository interfaces
- Use case interfaces (if needed)

**Rules**:
- ✅ No external dependencies (except Kotlin stdlib & coroutines)
- ✅ No framework-specific code
- ✅ Platform-agnostic
- ❌ No serialization annotations
- ❌ No network/database code

**Example**:
```kotlin
// Domain model - pure Kotlin
data class GenerateIdeaResponse(
    val idea: String,
    val type: String,
    val complexity: Complexity
)

// Repository interface - defines contract
interface IdeaRepository {
    suspend fun generateIdea(...): Result<GenerateIdeaResponse>
}
```

### 2. Data Layer (`core:data`)

**Purpose**: Implements domain contracts and manages data sources

**Contains**:
- Repository implementations
- Mappers (API ↔ Domain)
- Data source coordination logic
- Dependency injection configuration

**Rules**:
- ✅ Implements domain interfaces
- ✅ Maps between API and domain models
- ✅ Handles multiple data sources (API, cache, DB)
- ❌ No business logic
- ❌ No direct UI dependencies

**Example**:
```kotlin
// Repository implementation
class IdeaRepositoryImpl(
    private val api: IdeaGeneratorApi
) : IdeaRepository {
    override suspend fun generateIdea(...): Result<GenerateIdeaResponse> = 
        runCatching {
            api.generateIdea(...).toDomain()  // Map API → Domain
        }
}

// Mapper
fun ApiGenerateIdeaResponse.toDomain() = GenerateIdeaResponse(...)
```

### 3. API Layer (`core:api`)

**Purpose**: Handles network communication

**Contains**:
- Ktor client configuration
- Ktorfit API interface
- DTOs with serialization
- BuildKonfig configuration

**Rules**:
- ✅ Network-specific code only
- ✅ Serialization annotations on DTOs
- ✅ HTTP client configuration
- ❌ No business logic
- ❌ No domain model knowledge

**Example**:
```kotlin
// API DTO with serialization
@Serializable
data class GenerateIdeaResponse(
    val idea: String,
    @SerialName("time_estimate")
    val timeEstimate: String
)

// API client class
class IdeaGeneratorApi(private val client: HttpClient) {
    suspend fun generateIdea(request: GenerateIdeaRequest): GenerateIdeaResponse {
        return client.post("/generate_idea") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
```

## Benefits

### 1. **Testability**
- Domain layer can be tested without any framework
- Mock repository interfaces easily
- Test business logic in isolation

### 2. **Maintainability**
- Clear separation of concerns
- Changes in API don't affect domain
- Easy to understand and navigate

### 3. **Flexibility**
- Swap data sources without changing domain
- Replace API implementation easily
- Add caching layer without touching business logic

### 4. **Reusability**
- Domain models are pure Kotlin
- Repository interfaces can have multiple implementations
- Business logic is platform-agnostic

## Dependency Injection

### Setup

```kotlin
// In your app initialization
startKoin {
    modules(
        ApiModule().module,      // Provides IdeaGeneratorApi
        DataModule().module      // Provides IdeaRepository
    )
}
```

### Usage

```kotlin
// In ViewModel or UseCase
class GenerateIdeaViewModel(
    private val repository: IdeaRepository  // Domain interface
) {
    suspend fun generateIdea() {
        repository.generateIdea(
            niche = "AI",
            type = "startup"
        ).onSuccess { idea ->
            // Handle domain model
            println(idea.timeEstimate)  // camelCase
        }
    }
}
```

## Data Flow Example

```
User Action (UI)
    ↓
ViewModel calls repository.generateIdea()
    ↓
IdeaRepository interface (domain)
    ↓
IdeaRepositoryImpl (data) - calls API
    ↓
IdeaGeneratorApi (api) - makes HTTP request
    ↓
API Response (ApiGenerateIdeaResponse with time_estimate)
    ↓
Mapper converts to domain (GenerateIdeaResponse with timeEstimate)
    ↓
Result<GenerateIdeaResponse> returned to ViewModel
    ↓
UI updates with domain model
```

## Adding New Features

### 1. Add to Domain
```kotlin
// domain/model/NewFeature.kt
data class NewFeature(val name: String)

// domain/repository/IdeaRepository.kt
interface IdeaRepository {
    suspend fun getNewFeature(): Result<NewFeature>
}
```

### 2. Add to API
```kotlin
// api/models/NewFeatureDto.kt
@Serializable
data class NewFeatureDto(val name: String)

// api/IdeaGeneratorApi.kt
interface IdeaGeneratorApi {
    @GET("/new-feature")
    suspend fun getNewFeature(): NewFeatureDto
}
```

### 3. Add Mapper
```kotlin
// data/mapper/FeatureMapper.kt
fun NewFeatureDto.toDomain() = NewFeature(name = name)
```

### 4. Implement Repository
```kotlin
// data/repository/IdeaRepositoryImpl.kt
override suspend fun getNewFeature(): Result<NewFeature> = 
    runCatching {
        api.getNewFeature().toDomain()
    }
```

## Testing Strategy

### Domain Layer
```kotlin
// Pure unit tests - no mocking needed
@Test
fun `test domain model validation`() {
    val idea = GenerateIdeaResponse(...)
    assertTrue(idea.unique)
}
```

### Data Layer
```kotlin
// Mock API, test mapping and logic
@Test
fun `test repository maps API to domain`() {
    val mockApi = mockk<IdeaGeneratorApi>()
    val repository = IdeaRepositoryImpl(mockApi)
    // Test implementation
}
```

### API Layer
```kotlin
// Integration tests with mock server
@Test
fun `test API call`() {
    // Use MockWebServer or similar
}
```

## Migration Guide

If you have existing code using the old structure:

**Before**:
```kotlin
import com.yet.generate.api.repository.IdeaRepository
import com.yet.generate.api.models.*

val repository = IdeaRepository()
```

**After**:
```kotlin
import com.yet.generate.domain.repository.IdeaRepository
import com.yet.generate.domain.model.*

// Inject via Koin
class MyClass(private val repository: IdeaRepository)
```

## Summary

- **Domain**: Pure business logic, no dependencies
- **Data**: Implements domain interfaces, maps data
- **API**: Network layer, handles HTTP communication

This architecture ensures your business logic is independent, testable, and maintainable.
