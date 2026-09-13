## Context

See `proposal.md` for motivation. The project is an Android app targeting SDK 37, AGP 9.3.2, Gradle 9.5.0 using modern Gradle DSL (`compileSdk { version = release(37) }`, `optimization { enable = false }`). Currently, `app/build.gradle.kts` applies `kotlin.kapt` and `hilt.android`, but Room, Ktor, and KSP are missing or incomplete.

## Goals / Non-Goals

**Goals:**
- Configure version catalog (`libs.versions.toml`) and Kotlin DSL build files for Hilt, Room (with KSP), Ktor 3, Coil 3, and Navigation Compose.
- Provide a clean, modular architecture (Data/DI/UI) with repository pattern exposing `Flow` for Room and coroutine suspend functions for Ktor.
- Build reusable Material 3 Compose UI components with previews and accessibility metadata.
- Implement type-safe screen navigation with Kotlin Serialization.

**Non-Goals:**
- Implementation of complex multi-module architecture (single `:app` module is preserved).
- Production API backend integration (mock or public sample endpoint JSON mapping).

## Decisions

### 1. KSP for Room Annotation Processing
- **Decision:** Use Google KSP plugin for Room code generation rather than legacy kapt.
- **Rationale:** KSP is significantly faster and actively maintained for Kotlin 2.0+. Kapt will be kept for Hilt (or Hilt KSP if stable).

### 2. Ktor Client with Android Engine
- **Decision:** Use `ktor-client-android`, `ktor-client-content-negotiation`, `ktor-serialization-kotlinx-json`, `ktor-client-logging`, and `ktor-client-timeout`.
- **Rationale:** Lightweight, coroutine-native HTTP client integrated cleanly with `kotlinx.serialization`.

### 3. Type-Safe Navigation Compose
- **Decision:** Define navigation destinations using `@Serializable` data objects and data classes with `androidx.navigation.compose`.
- **Rationale:** Type-safe navigation eliminates string route parsing bugs and string-based argument extraction.

### 4. Coil 3 for Compose Image Loading
- **Decision:** Integrate `io.coil-kt.coil3:coil-compose` and `io.coil-kt.coil3:coil-network-ktor3` (or okhttp).
- **Rationale:** Coil 3 is the modern image loading library designed specifically for Compose Multiplatform and Android.

## Risks / Trade-offs

- [AGP 9.3.2 / Kotlin 2.0 DSL Compatibility] → Verify plugin compatibility in `libs.versions.toml` and test build with `.\gradlew.bat assembleDebug`.
- [Room Schema Export Warning] → Set `room.schemaLocation` in KSP arguments or disable schema export for template database.

## Migration Plan

1. Update `gradle/libs.versions.toml`, `build.gradle.kts`, and `app/build.gradle.kts`.
2. Add KSP plugin and Room dependencies.
3. Build Hilt DI modules and Data components (Entity, DAO, DB, API, Repository).
4. Build reusable UI components and type-safe Navigation graph.
5. Verify build with `.\gradlew.bat assembleDebug`.
