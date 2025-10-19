# API Module Setup Guide

## Quick Start

1. **Copy the example properties file:**
   ```bash
   cp secret.properties.example secret.properties
   ```

2. **Configure your API URL in `secret.properties`:**
   ```properties
   API_BASE_URL=http://127.0.0.1:8001
   ```

3. **Build the project:**
   ```bash
   ./gradlew :core:api:build
   ```

## What's Configured

- ✅ BuildKonfig plugin for compile-time configuration
- ✅ API base URL loaded from `secret.properties`
- ✅ `secret.properties` added to `.gitignore`
- ✅ Example file `secret.properties.example` for team reference
- ✅ Koin DI module with automatic configuration

## Usage in Code

The `BuildKonfig.API_BASE_URL` is automatically used in:
- `ApiClientBuilder` default parameter
- `ApiModule` Koin provider
- `ApiClient` singleton object

You don't need to manually pass the URL unless you want to override it.

## Security

The `secret.properties` file is git-ignored to prevent accidentally committing sensitive URLs or API keys. Share the `.example` file with your team instead.
