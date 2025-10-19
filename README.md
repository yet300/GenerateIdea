# GenerateIdea

https://github.com/user-attachments/assets/e65d25dc-b1b7-4bcc-b42e-cc3b966d8832



https://github.com/user-attachments/assets/65c79d2b-b63e-4a1f-bb9c-bd286a725071


A Kotlin Multiplatform AI-powered idea generator built in two days(Client and Backend) at a hackathon(Hacktoberfes) with vibe coding(Kiro Ide with Claude Sonnet 4.5). This project demonstrates clean architecture principles across Android, iOS, and Desktop platforms using Compose Multiplatform and SwiftUI for ios.

## What is this?

This is a hackathon project created in just a few hours that showcases:
- AI-powered idea generation for startups, apps, and projects
- Clean architecture with proper separation of concerns
- Kotlin Multiplatform targeting Android, iOS, and Desktop (JVM)
- Modern UI with Compose Multiplatform and Swift UI
- MVI architecture with MVIKotlin and Decompose
- Reactive state management

## Tech Stack

- **Kotlin Multiplatform** - Share code across platforms
- **Compose Multiplatform** - Unified UI framework
- **Swift UI** - Native UI framework for Apple devices
- **Decompose** - Navigation and lifecycle management
- **MVIKotlin** - MVI architecture implementation
- **Ktor** - HTTP client for API calls
- **Koin** - Dependency injection
- **kotlinx.serialization** - JSON serialization
- **BuildKonfig** - Configuration management

## Project Structure

```
GenerateIdea/
├── composeApp/          # Main application UI (Android, iOS, Desktop)
├── shared/              # Shared business logic
├── core/                # Clean architecture core modules
│   ├── api/            # Network layer (Ktor, DTOs)
│   ├── domain/         # Business logic (models, interfaces)
│   ├── data/           # Repository implementations
│   └── common/         # Common utilities
├── feature/            # Feature modules
│   ├── chat/          # Chat/idea generation feature
│   └── root/          # Root navigation
├── iosApp/            # iOS-specific entry point
└── build-logic/       # Gradle convention plugins
```

## Architecture

This project follows **Clean Architecture** principles with clear separation of concerns:

```
Presentation (UI) → Domain (Business Logic) → Data (Repository) → API (Network)
```

See [core/ARCHITECTURE.md](./core/ARCHITECTURE.md) for detailed architecture documentation.

### Key Principles

- **Domain layer** is pure Kotlin with no external dependencies
- **Data layer** implements domain interfaces and maps between API and domain models
- **API layer** handles network communication with Ktor
- **Feature modules** are self-contained with their own UI, state management, and components

## Prerequisites

- **JDK 17 or higher** - Required for Kotlin and Gradle
- **Android Studio** (for Android development)
- **Xcode** (for iOS development, macOS only)
- **Gradle** (included via wrapper)

## Required Configuration Files

### 1. secret.properties

Create a `secret.properties` file in the project root with your API configuration:

```properties
# API Configuration
# For emulator/simulator use: http://127.0.0.1:8001 or http://10.0.2.2:8001 (Android emulator)
# For physical devices use your computer's local IP: http://192.168.1.XXX:8001
API_BASE_URL=http://127.0.0.1:8001
```

**Important**: This file is gitignored. Each developer needs to create their own.

### 2. Backend API Server

This app requires a backend API server running locally. The API should expose the following endpoint:

- `POST /generate_idea` - Generate ideas based on niche, type, and complexity

Expected request format:
```json
{
  "niche": "AI",
  "type": "startup",
  "complexity": "medium"
}
```

Expected response format:
```json
{
  "idea": "Your generated idea...",
  "time_estimate": "2-3 months",
  "unique": true,
  "complexity": "medium"
}
```

## How to Run

### Android

**Option 1: Android Studio**
1. Open the project in Android Studio
2. Select the `composeApp` run configuration
3. Choose an Android device or emulator
4. Click Run

**Option 2: Command Line**
```bash
# Build debug APK
./gradlew :composeApp:assembleDebug

# Install and run on connected device
./gradlew :composeApp:installDebug
```

### Desktop (JVM)

**Option 1: IDE**
1. Open the project in IntelliJ IDEA or Android Studio
2. Select the `composeApp` desktop run configuration
3. Click Run

**Option 2: Command Line**
```bash
# Run desktop application
./gradlew :composeApp:run

# Build distributable packages
./gradlew :composeApp:packageDistributionForCurrentOS
```

Packages will be created in `composeApp/build/compose/binaries/main/`:
- **macOS**: `.dmg` file
- **Windows**: `.msi` file
- **Linux**: `.deb` file

### iOS

**Option 1: Xcode**
1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select a simulator or device
3. Click Run

**Option 2: IDE**
1. Open the project in Android Studio with KMM plugin
2. Select the iOS run configuration
3. Choose a simulator
4. Click Run

## Development

### Project Modules

- **composeApp** - Main UI application for all platforms
- **shared** - Shared business logic and utilities
- **core:api** - Network layer with Ktor client
- **core:domain** - Business models and repository interfaces
- **core:data** - Repository implementations and mappers
- **core:common** - Common utilities and extensions
- **feature:chat** - Chat/idea generation feature with MVI store
- **feature:root** - Root navigation component

### Adding New Features

1. Create domain models in `core:domain`
2. Add API DTOs and endpoints in `core:api`
3. Implement repository in `core:data`
4. Create feature module with UI and state management
5. Wire up with Koin dependency injection

See [core/ARCHITECTURE.md](./core/ARCHITECTURE.md) for detailed guidelines.

## Troubleshooting

### API Connection Issues

- **Emulator/Simulator**: Use `http://127.0.0.1:8001` or `http://10.0.2.2:8001` (Android)
- **Physical Device**: Use your computer's local IP (e.g., `http://192.168.1.100:8001`)
- Ensure your backend server is running and accessible
- Check firewall settings if using physical devices

### Build Issues

```bash
# Clean build
./gradlew clean

# Rebuild project
./gradlew build --refresh-dependencies
```

### iOS Build Issues

- Ensure Xcode Command Line Tools are installed: `xcode-select --install`
- Clean Xcode build folder: Product → Clean Build Folder
- Delete derived data: `rm -rf ~/Library/Developer/Xcode/DerivedData`

## Project Status

This is a hackathon project built in two days with vibe coding. It demonstrates:
- Rapid prototyping with Kotlin Multiplatform
- Clean architecture in a real-world scenario
- Modern Android development practices
- Cross-platform UI with Compose

The architecture is production-ready and can be extended with additional features, testing, and polish.
---
