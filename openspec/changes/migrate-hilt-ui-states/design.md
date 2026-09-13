## Context

See `proposal.md` for motivation. The application currently uses a manual `AppContainer` for dependency management that suffers from missing constructor parameters (`ExampleMapper`) and manual passing of dependencies down through `AppNavHost`.

## Goals / Non-Goals

**Goals:**
- Replace manual `AppContainer` with Hilt Dependency Injection across data, domain, and presentation layers.
- Fix compilation issues by converting `ExampleMapper` to an injectable component and injecting it into `ExampleRepositoryImpl`.
- Enable `hiltViewModel()` injection in `HomeScreen` and Jetpack Compose composables.
- Model UI state comprehensively with explicit Loading, Error, Empty, and Success states and reusable UI components.

**Non-Goals:**
- Replacing existing Jetpack Compose Navigation structure with external routing libraries.
- Modifying offline storage implementations beyond DI configuration.

## Decisions

### 1. Hilt Plugin Application & Dependency Configuration
- **Decision**: Apply `org.jetbrains.kotlin.kapt` and `com.google.dagger.hilt.android` plugins in `app/build.gradle.kts` and add `androidx.hilt:hilt-navigation-compose` for `hiltViewModel()`.
- **Rationale**: Standard Android architecture approach for DI in Compose apps.
- **Alternatives**: Koin or manual factory delegation (rejected to align with standard Android catalog & Hilt support).

### 2. Injectable Mapper Class
- **Decision**: Convert `ExampleMapper` from top-level functions or un-injected class to `@Inject constructor() class ExampleMapper`. Inject it into `ExampleRepositoryImpl` constructor.
- **Rationale**: Resolves missing dependency injection compilation errors and enforces testability and modular mapping logic.
- **Alternatives**: Retain static extension functions (rejected as user explicitly requested correcting the invalid dependency injection of `ExampleMapper`).

### 3. Repository & Data Hilt Module (`DataModule`)
- **Decision**: Create `@Module @InstallIn(SingletonComponent::class) abstract class DataModule` with `@Binds` for `ExampleRepository` -> `ExampleRepositoryImpl`. Use `@Inject constructor()` for `ExampleLocalDataSource` and `GetExampleItemsUseCase`.
- **Rationale**: Decouples domain interface contracts from data implementations cleanly.

### 4. Sealed / Structured UI State Hierarchy
- **Decision**: Represent `HomeUiState` using a sealed interface or data class with discrete states (`Loading`, `Error`, `Empty`, `Success`).
- **Rationale**: Prevents illegal UI state combinations (e.g. loading = true while error is set) and makes UI rendering deterministic.
- **Alternatives**: Multiple boolean flags (rejected due to invalid combined states).

## Risks / Trade-offs

- **[Risk] kapt build overhead with Gradle AGP 9.3+** → Keep kapt configuration lightweight and scoped to `:app`.
- **[Risk] Preview breakage when using `hiltViewModel()` in @Preview** → Wrap composables into stateful and stateless (content) composables so previews can pass dummy UI state.
