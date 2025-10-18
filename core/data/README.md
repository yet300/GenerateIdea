# Data Module

This module contains the **data layer implementation** following Clean Architecture principles.

## Structure

```
data/
├── di/
│   └── DataModule.kt         # Koin DI configuration
├── mapper/
│   └── IdeaMapper.kt         # API ↔ Domain model mappers
└── repository/
    └── IdeaRepositoryImpl.kt # Repository implementation
```

## Responsibilities

- **Implement domain repository interfaces**
- **Map API models to domain models**
- **Handle data source coordination** (API, cache, database)
- **Provide dependency injection configuration**

## Architecture Flow

```
Domain (Interface) ← Data (Implementation) ← API (Network)
     ↑                      ↑                     ↑
  Business Logic      Mappers & Repo         Ktor Client
```

## Mappers

Mappers convert between API models (with serialization) and domain models (pure Kotlin):

```kotlin
// API → Domain
fun ApiGenerateIdeaResponse.toDomain() = GenerateIdeaResponse(...)

// Domain → API
fun Complexity.toApi() = ApiComplexity(...)
```

## Repository Implementation

`IdeaRepositoryImpl` implements the `IdeaRepository` interface from the domain module:

```kotlin
class IdeaRepositoryImpl(
    private val api: IdeaGeneratorApi
) : IdeaRepository {
    override suspend fun generateIdea(...): Result<GenerateIdeaResponse> = 
        runCatching {
            api.generateIdea(...).toDomain()
        }
}
```

## Dependency Injection

The `DataModule` provides repository implementations:

```kotlin
@Module
class DataModule

@Single
fun provideIdeaRepository(api: IdeaGeneratorApi): IdeaRepository {
    return IdeaRepositoryImpl(api)
}
```

## Usage

In your app initialization:

```kotlin
startKoin {
    modules(
        ApiModule().module,
        DataModule().module
    )
}
```

Then inject the domain interface:

```kotlin
class MyViewModel(
    private val repository: IdeaRepository  // Domain interface
) {
    // Implementation is provided by DataModule
}
```

## Dependencies

- `core:api` - Network layer
- `core:domain` - Business logic interfaces
- `koin` - Dependency injection
