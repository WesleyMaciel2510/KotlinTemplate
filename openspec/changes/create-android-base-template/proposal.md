## Why

Creating a complete, compilable Android project base template that can be reused across multiple future apps. The current project is an empty template with no source code, no Activity, and no Kotlin plugin applied. This change establishes a production-ready architectural foundation with MVVM + Clean Architecture, Jetpack Compose, Material 3, type-safe navigation, and all modern Android best practices baked in.

## What Changes

- Add Kotlin 2.3.20 with K2 compiler and Android Gradle Plugin 9.3.0
- Configure Gradle version catalog (libs.versions.toml) for all dependencies
- Set up compileSdk=36, targetSdk=36, minSdk=26 with JDK 21 toolchain
- Create complete project structure with three-layer architecture:
  - presentation/ — Compose UI, ViewModels, UiState, UiEvent
  - domain/ — Use cases, repository interfaces, domain models
  - data/ — Repository implementations, data sources, DTOs, mappers
- Implement manual DI via AppContainer (no framework, structured for future DI adoption)
- Add type-safe Navigation Compose with kotlinx.serialization routes
- Add Baseline Profiles module with Macrobenchmark generation
- Configure release build with R8 full mode, minification, and ProGuard rules
- Implement minimal end-to-end "Home" feature proving all layers wired correctly

## Capabilities

### New Capabilities
- `android-template`: Complete reusable Android base template with MVVM + Clean Architecture, Compose, Material 3, type-safe navigation, and baseline profiles

### Modified Capabilities
- None (no existing capabilities in this project)

## Impact

- New files: ~30 source files across app and baselineprofile modules
- Modified files: build.gradle.kts (root and app), settings.gradle.kts, libs.versions.toml, gradle.properties
- Dependencies: All version-catalog managed (Compose BOM, Navigation, Serialization, Coroutines, Lifecycle, Baseline Profiles)
- No breaking changes (greenfield project)