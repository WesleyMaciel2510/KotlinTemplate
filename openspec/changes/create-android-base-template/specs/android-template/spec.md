## Purpose

Provides a complete, compilable Android base template with MVVM + Clean Architecture, Jetpack Compose, Material 3, type-safe navigation, and Baseline Profiles that can be reused across multiple future apps.

## ADDED Requirements

### Requirement: Project compiles and builds successfully
The template project SHALL compile without errors using Kotlin 2.3.20 (K2 compiler), AGP 9.3.0, Gradle compatible with AGP 9.3.0, and JDK 21 toolchain.

#### Scenario: Debug build succeeds
- **WHEN** running `./gradlew.bat assembleDebug`
- **THEN** the build completes successfully with no compilation errors

#### Scenario: Release build succeeds with optimizations
- **WHEN** running `./gradlew.bat assembleRelease`
- **THEN** the build completes successfully with R8 full mode, minification, and shrink resources enabled

### Requirement: Version catalog manages all dependencies
The project SHALL use a Gradle version catalog (`gradle/libs.versions.toml`) as the single source of truth for all dependency and plugin versions, with no hardcoded version strings in any `build.gradle.kts` file.

#### Scenario: All dependencies resolved from catalog
- **WHEN** inspecting any module `build.gradle.kts`
- **THEN** all dependencies reference version catalog entries (e.g., `libs.androidx.compose.bom`) rather than literal version strings

### Requirement: Three-layer Clean Architecture enforced
The codebase SHALL be organized into three layers with strict one-way dependency rule: `presentation → domain ← data` (domain has zero Android/framework imports).

#### Scenario: Domain layer has no Android dependencies
- **WHEN** analyzing `domain/` module imports
- **THEN** no `android.*`, `androidx.*`, or Compose imports exist in domain source files

#### Scenario: Presentation depends only on domain
- **WHEN** analyzing `presentation/` module imports
- **THEN** presentation imports only domain interfaces/models, never data layer implementations

#### Scenario: Data layer implements domain interfaces
- **WHEN** analyzing `data/` module imports
- **THEN** data imports domain repository interfaces and implements them

### Requirement: MVVM with unidirectional data flow
The presentation layer SHALL use ViewModels exposing immutable `StateFlow<UiState>` where `UiState` is an immutable data class, and Composables send `UiEvent` sealed interfaces to ViewModels for state mutation.

#### Scenario: ViewModel exposes immutable state
- **WHEN** inspecting ViewModel public API
- **THEN** `val state: StateFlow<UiState>` is exposed (never `MutableStateFlow`), and `UiState` is a `data class` with all `val` properties

#### Scenario: Composables never mutate state directly
- **WHEN** inspecting Composable code
- **THEN** Composables only call `viewModel.onEvent(UiEvent)` and observe `viewModel.state` for recomposition

### Requirement: 100% Jetpack Compose with Material 3
The UI SHALL use only Jetpack Compose with Material 3 components (`androidx.compose.material3.*`), with zero XML layouts and zero View-system code.

#### Scenario: No XML layouts exist
- **WHEN** searching for `.xml` files in `app/src/main/res/layout/`
- **THEN** no layout XML files are found

#### Scenario: Only Material 3 imports used
- **WHEN** inspecting Composable imports
- **THEN** only `androidx.compose.material3.*` imports are used for UI components (no Material 2)

### Requirement: Type-safe Navigation with serialization
Navigation SHALL use a single `NavHost` in `MainActivity` with `@Serializable` route objects/data classes passed as typed arguments, not string-concatenated routes.

#### Scenario: Routes are type-safe serializable objects
- **WHEN** inspecting route definitions
- **THEN** routes are defined as `@Serializable` data classes/objects in `Routes.kt`

#### Scenario: Navigation arguments are typed
- **WHEN** navigating to a destination with arguments
- **THEN** arguments are passed as typed objects and retrieved via `getInt()`/`getString()` with compile-time safety

### Requirement: Repository pattern with manual DI
Repository interfaces SHALL be declared in `domain/repository/`, implementations in `data/repository/`, injected into ViewModels via constructor through a lightweight `AppContainer` class.

#### Scenario: Repository interface in domain
- **WHEN** inspecting `domain/repository/`
- **THEN** repository interfaces are defined with no implementation details

#### Scenario: Repository implementation in data
- **WHEN** inspecting `data/repository/`
- **THEN** implementations exist and implement domain interfaces

#### Scenario: AppContainer wires dependencies manually
- **WHEN** inspecting `AppContainer`
- **THEN** all dependencies are instantiated and provided via constructor injection (no DI framework)

### Requirement: kotlinx.serialization for DTOs and navigation
All DTO/JSON models and Navigation route arguments SHALL use `kotlinx.serialization` (no Gson, no Moshi).

#### Scenario: DTOs are serializable
- **WHEN** inspecting data DTO classes
- **THEN** they are annotated with `@Serializable`

#### Scenario: Route arguments are serializable
- **WHEN** inspecting route classes
- **THEN** they are annotated with `@Serializable`

### Requirement: Baseline Profiles generated and bundled
A separate `:baselineprofile` Gradle module SHALL generate Baseline Profiles using Macrobenchmark `BaselineProfileRule`, the `androidx.baselineprofile` plugin SHALL be applied to `:app`, and the generated `baseline-prof.txt` SHALL be bundled into release APK/AAB.

#### Scenario: Baseline profile module exists
- **WHEN** inspecting project modules
- **THEN** `:baselineprofile` module is present with `BaselineProfileGenerator`

#### Scenario: Profile bundled in release build
- **WHEN** building release APK/AAB
- **THEN** `src/main/baseline-prof.txt` is included in the output

### Requirement: ProGuard rules for kotlinx.serialization
Release build SHALL include `proguard-rules.pro` with keep rules for `kotlinx.serialization` serializers.

#### Scenario: Serialization keep rules present
- **WHEN** inspecting `app/proguard-rules.pro`
- **THEN** keep rules for kotlinx.serialization serializers are defined

### Requirement: Minimal end-to-end Home feature proves all layers
A single `HomeScreen` Composable SHALL fetch `ExampleItem` list through `GetExampleItemsUseCase` → `ExampleRepository` → `ExampleLocalDataSource` (in-memory fake data) → display in a `LazyColumn` with Material 3 `Scaffold`, `TopAppBar`, `Card`, `Text`.

#### Scenario: Home screen displays items from repository
- **WHEN** launching the app
- **THEN** `HomeScreen` shows a list of `ExampleItem` in a `LazyColumn` with Material 3 styling

#### Scenario: All architectural layers exercised
- **WHEN** tracing the data flow for Home screen
- **THEN** the flow goes: `HomeScreen` → `HomeViewModel.onEvent` → `GetExampleItemsUseCase` → `ExampleRepository` → `ExampleLocalDataSource` → domain model → `HomeUiState` → recomposition