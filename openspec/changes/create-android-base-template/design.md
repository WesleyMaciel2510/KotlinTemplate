## Context

Current project is an empty Android template (`com.example.kotlin`) with no source code, no Activity, and no Kotlin plugin applied. The AGENTS.md specifies: single-module app (`:app`), namespace `com.example.kotlin`, version catalog at `gradle/libs.versions.toml`, AGP 9.3.2/Gradle 9.5.0 with new DSL, Java 25 daemon toolchain with Java 11 source/target compatibility, configuration cache enabled, `RepositoriesMode.FAIL_ON_PROJECT_REPOS`.

The encrypt.md prompt provides the full specification for the desired template: Kotlin 2.3.20, AGP 9.3.0, compileSdk=36, Compose BOM pinned to last version supporting compileSdk 36, Material 3, MVVM + Clean Architecture, type-safe Navigation, kotlinx.serialization, Baseline Profiles, R8 full mode.

## Goals / Non-Goals

**Goals:**
- Create complete, compilable Android base template at `com.template.app` package
- Implement all architectural layers with working Home feature proving the stack
- Configure version catalog as single source of truth
- Set up Baseline Profiles module with profile generation
- Enable release optimizations (R8 full mode, minification, ProGuard)

**Non-Goals:**
- Unit tests, instrumented tests, CI configuration
- Networking libraries, analytics, real backend integration
- DI framework (Hilt/Koin/Dagger) — manual AppContainer only
- Multiple build flavors or product flavors
- Documentation beyond code comments

## Decisions

### 1. Compose BOM Version: 2026.07.00
**Rationale**: Prompt specifies compileSdk=36 hard requirement. Compose 1.12 / BOM 2026.08.00+ requires compileSdk 37. The BOM immediately prior is 2026.07.00 (Compose 1.11.x).
**Alternative considered**: Using 2026.08.00 with compileSdk 37 — rejected due to hard constraint.

### 2. Two-Module Structure: `:app` + `:baselineprofile`
**Rationale**: Baseline Profiles require a separate module for Macrobenchmark generation per Android best practices. The `androidx.baselineprofile` plugin applies to `:app` and consumes profiles from `:baselineprofile`.
**Alternative considered**: Single module with baseline profile generation — rejected; separate module is required for proper Macrobenchmark isolation.

### 3. Package Root: `com.template.app`
**Rationale**: Prompt explicitly specifies this placeholder; human renames per-project.
**Alternative considered**: Keeping `com.example.kotlin` — rejected per requirements.

### 4. Manual DI via `AppContainer` Class
**Rationale**: Prompt explicitly prohibits Hilt/Koin/Dagger but requires structure allowing future DI adoption. A single `AppContainer` with lazy-initialized singletons satisfies this.
**Alternative considered**: Service locator pattern — rejected; constructor injection via container is more testable and explicit.

### 5. Navigation: `@Serializable` Route Objects with `navigation-compose` 2.8+
**Rationale**: Prompt requires type-safe routes via kotlinx.serialization. Navigation Compose 2.8+ supports `Serializable` route classes directly.
**Alternative considered**: String routes with manual parsing — rejected; not type-safe.

### 6. StateFlow<UiState> in ViewModels, UiEvent Sealed Interfaces
**Rationale**: Prompt mandates immutable `StateFlow<UiState>` exposed publicly, `UiState` as all-val data class, `UiEvent` as sealed interface for unidirectional flow.
**Alternative considered**: MutableStateFlow or LiveData — rejected; violates explicit constraints.

### 7. Gradle Configuration: Version Catalog + New AGP DSL
**Rationale**: AGENTS.md mandates version catalog, AGP 9.3.2 uses new DSL (`compileSdk { version = release(37) }` style). We'll use `compileSdk = 36` in libs.versions.toml and reference it.
**Alternative considered**: Hardcoded versions in build.gradle.kts — rejected; violates version catalog requirement.

### 8. JDK Toolchain: 21 (per prompt) vs 25 (AGENTS.md daemon)
**Rationale**: Prompt explicitly requires `kotlin { jvmToolchain(21) }`. AGENTS.md notes daemon is Java 25 but source/target compatibility is Java 11. We'll configure toolchain 21 in app module.
**Alternative considered**: Using Java 25 toolchain — rejected; prompt is explicit.

### 9. Baseline Profile Generation: Macrobenchmark with `BaselineProfileRule`
**Rationale**: Prompt specifies `androidx.benchmark:benchmark-macro-junit4` in `:baselineprofile` module, `androidx.baselineprofile` plugin on `:app`, generates `src/main/baseline-prof.txt` bundled in release.
**Alternative considered**: Manual profile creation — rejected; not maintainable.

### 10. Release Build: R8 Full Mode + ProGuard Rules
**Rationale**: Prompt requires `isMinifyEnabled = true`, `isShrinkResources = true`, `android.enableR8.fullMode=true` in gradle.properties, consumer/app proguard-rules.pro with kotlinx.serialization keep rules.
**Alternative considered**: Default R8 mode — rejected; full mode is explicit requirement.

## Risks / Trade-offs

| Risk | Mitigation |
|------|------------|
| Compose BOM 2026.07.00 may have unresolved bugs fixed in later BOMs | Pin to specific patch version in version catalog; can upgrade when compileSdk 37 is acceptable |
| Manual DI container becomes unwieldy as app grows | Document clear migration path to Hilt/Koin; container structure mirrors DI graph |
| Configuration cache + new AGP DSL may have edge cases | Test build with `--configuration-cache` locally; avoid task-time project access |
| Baseline profile generation requires physical device/emulator | Document CI requirements; provide local generation instructions |
| Java 21 toolchain vs Java 25 daemon mismatch | Toolchain only affects compilation; daemon version is transparent |
| No tests in template | Non-goal per requirements; consumers add their own test infrastructure |

## Migration Plan

1. Apply all build configuration changes (version catalog, plugins, toolchain)
2. Create directory structure for all three layers
3. Implement domain layer (models, repository interfaces, use cases)
4. Implement data layer (repository impl, data sources, DTOs, mappers)
5. Implement presentation layer (ViewModels, UiState, UiEvent, Composables)
6. Wire navigation and AppContainer
7. Add Baseline Profile module and generator
8. Configure release build optimizations
9. Verify debug and release builds succeed

Rollback: Revert all changes; original empty template remains functional.

## Open Questions

- None — all major decisions resolved by explicit constraints in prompt and AGENTS.md