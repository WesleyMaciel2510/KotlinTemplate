# AI PROMPT: Generate Reusable Kotlin/Android Base Template

## ROLE
You are generating a complete, compilable Android project used as a reusable base template for spinning up multiple future apps. Output the full file tree, then full contents of every file. No TODOs, no placeholder logic beyond one minimal end-to-end "Home" feature that proves every architectural layer wired correctly. Package root placeholder: `com.template.app` — assume the human will rename it per-project.

## VERSION MATRIX (verify each against source before writing build files — do not silently use older cached versions)
| Component | Pin | Source of truth |
|---|---|---|
| Kotlin | 2.3.20 (latest stable; K2 compiler is default) | kotlinlang.org/docs/releases.html |
| Android Gradle Plugin (AGP) | 9.3.0 (bundles Kotlin Gradle Plugin support natively) | developer.android.com/build/releases |
| Gradle | latest version satisfying AGP 9.3.0's minimum compatibility requirement | AGP compatibility table |
| JDK / JVM toolchain | 21 | `kotlin { jvmToolchain(21) }` |
| compileSdk / targetSdk | 36 (Android 16) — hard requirement, do not raise | — |
| minSdk | 26 | — |
| Compose BOM | pin to the newest BOM release that still supports `compileSdk = 36`. **Constraint conflict to resolve:** Compose 1.12 / BOM `2026.08.00`+ raised its required compileSdk to 37. Since compileSdk is pinned at 36 here, select the BOM release immediately prior to that bump. Confirm via the official BOM-to-library mapping page before finalizing. | developer.android.com/develop/ui/compose/bom |
| Material 3 | resolved transitively via Compose BOM (`androidx.compose.material3:material3`) | — |
| Navigation Compose | latest stable `androidx.navigation:navigation-compose` supporting type-safe routes via `kotlinx.serialization` (2.8+) | — |
| kotlinx.serialization | 1.11.0 (`kotlinx-serialization-json`) | — |
| kotlinx.coroutines | latest stable 1.x (`kotlinx-coroutines-android`) | — |
| Lifecycle (ViewModel/StateFlow integration) | latest stable `androidx.lifecycle:lifecycle-viewmodel-compose`, `lifecycle-runtime-compose` | — |
| Baseline Profiles | `androidx.profileinstaller:profileinstaller` + `androidx.baselineprofile` Gradle plugin + `androidx.benchmark:benchmark-macro-junit4` in a separate `:baselineprofile` module | — |

Use a Gradle **version catalog** (`gradle/libs.versions.toml`) for every dependency above — no hardcoded version strings inside module `build.gradle.kts` files.

## NON-NEGOTIABLE CONSTRAINTS
1. Kotlin 2.x, K2 compiler enabled (default — do not set `languageVersion` below 2.0).
2. `compileSdk = 36`, `targetSdk = 36`, `minSdk = 26`.
3. UI: 100% Jetpack Compose. Zero XML layouts, zero View-system code.
4. Design system: Material 3 only (`androidx.compose.material3.*`). No Material 2 imports.
5. Architecture: MVVM + Clean Architecture, three layers, one-way dependency rule `presentation → domain ← data` (domain has zero Android/framework imports):
    - `presentation/` — Composables, ViewModels, immutable `UiState` data classes, `UiEvent` sealed interfaces.
    - `domain/` — use cases (single `operator fun invoke()` classes), repository interfaces, plain Kotlin domain models.
    - `data/` — repository implementations, remote/local data sources, DTOs + mappers to domain models.
6. Concurrency: Kotlin Coroutines everywhere (no RxJava, no callbacks, no raw Thread).
7. State: `StateFlow<UiState>` exposed from `ViewModel` as `val state: StateFlow<UiState>` (never expose `MutableStateFlow` publicly). `UiState` is an immutable `data class` (all `val`, no `var`, collections as `List`/`Map` not `MutableList`).
8. Unidirectional Data Flow: Composable → `ViewModel.onEvent(UiEvent)` → state mutation inside ViewModel only → new immutable `UiState` → recomposition. Composables never mutate state directly.
9. Navigation: single `MainActivity : ComponentActivity` hosting one `NavHost`. All other screens are Composable destinations, zero additional Activities. Routes are `@Serializable` Kotlin objects/data classes passed as typed arguments (not string-concatenated routes).
10. Repository pattern: interface declared in `domain/repository/`, implementation in `data/repository/`, injected into ViewModels via constructor.
11. Dependency wiring: manual constructor injection via a single lightweight `AppContainer` class (no Hilt/Koin/Dagger — intentionally omitted since not in scope; structure the container so a DI framework could be dropped in later without restructuring layers).
12. Serialization: `kotlinx.serialization` for any DTO/JSON model and for Navigation route arguments. No Gson, no Moshi.
13. Build: Gradle Kotlin DSL only (`build.gradle.kts`, `settings.gradle.kts`) — no Groovy files anywhere.
14. Release build type: `isMinifyEnabled = true`, `isShrinkResources = true`, R8 full mode enabled (`android.enableR8.fullMode=true` in `gradle.properties`), consumer/app `proguard-rules.pro` present with keep rules for `kotlinx.serialization` serializers.
15. Baseline Profiles: separate `:baselineprofile` Gradle module using Macrobenchmark `BaselineProfileRule`, `androidx.baselineprofile` plugin applied to `:app`, generates `src/main/baseline-prof.txt` bundled into release APK/AAB, plus `androidx.profileinstaller` runtime dependency in `:app`.

## OUTPUT: PROJECT FILE TREE (generate exactly this shape, package `com.template.app`)
```
settings.gradle.kts
build.gradle.kts                          (root — plugin versions only, no `apply`)
gradle/libs.versions.toml
gradle.properties
app/build.gradle.kts
app/proguard-rules.pro
app/src/main/AndroidManifest.xml
app/src/main/java/com/template/app/
  MainActivity.kt
  TemplateApp.kt                          (Application class + AppContainer instantiation)
  di/AppContainer.kt
  navigation/AppNavHost.kt
  navigation/Routes.kt                    (@Serializable route objects)
  ui/theme/Color.kt
  ui/theme/Theme.kt
  ui/theme/Type.kt
  presentation/home/HomeViewModel.kt
  presentation/home/HomeUiState.kt
  presentation/home/HomeUiEvent.kt
  presentation/home/HomeScreen.kt
  domain/model/ExampleItem.kt
  domain/repository/ExampleRepository.kt
  domain/usecase/GetExampleItemsUseCase.kt
  data/repository/ExampleRepositoryImpl.kt
  data/datasource/ExampleLocalDataSource.kt
  data/dto/ExampleItemDto.kt
  data/mapper/ExampleMapper.kt
baselineprofile/build.gradle.kts
baselineprofile/src/main/java/com/template/app/baselineprofile/BaselineProfileGenerator.kt
```

## GENERATION RULES
- Every file must compile together as one project — resolve imports consistently, no orphaned references.
- `HomeScreen` is the only screen: fetch `ExampleItem` list through `GetExampleItemsUseCase` → `ExampleRepository` → `ExampleLocalDataSource` (in-memory fake data, no network) → display in a `LazyColumn` using Material 3 components (`Scaffold`, `TopAppBar`, `Card`, `Text`). This single flow must visibly exercise every layer and every architectural rule above.
- `HomeUiState` must include at minimum: `isLoading: Boolean`, `items: List<ExampleItem>`, `error: String?` — all `val`.
- `AppNavHost` must define at least two typed destinations (`Routes.Home`, `Routes.Detail(id: Int)`) to demonstrate type-safe argument passing, even though only `Home` needs a real screen (`Detail` can render a placeholder Text composable).
- Do not add: unit tests, CI config, README, sample business logic beyond the example above, analytics, networking libraries, or any dependency not listed in the version matrix.
- After the file tree, output each file's full contents in the same order as the tree, each in its own fenced code block labeled with the correct language/filename.