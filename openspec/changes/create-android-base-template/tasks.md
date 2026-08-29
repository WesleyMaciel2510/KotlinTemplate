## 1. Build Configuration & Project Setup

- [x] 1.1 Update `gradle/libs.versions.toml` with all required dependencies and plugins (Kotlin 2.3.20, AGP 9.3.0, Compose BOM 2026.07.00, Navigation Compose 2.8+, kotlinx.serialization 1.11.0, Coroutines, Lifecycle, Baseline Profile plugins) and verify catalog parses correctly
- [x] 1.2 Update root `build.gradle.kts` with plugin versions from catalog (no `apply`), configure `plugins { id("com.android.application") version libs.plugins.android.application apply false }` etc., and verify `./gradlew.bat projects` lists modules
- [x] 1.3 Update `settings.gradle.kts` to include `:app` and `:baselineprofile` modules, configure `repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)`, and verify `./gradlew.bat projects` shows both modules
- [x] 1.4 Update `gradle.properties` with `android.enableR8.fullMode=true`, `org.gradle.jvmargs=-Xmx2048m`, and verify properties are read during build
- [x] 1.5 Configure `app/build.gradle.kts` with AGP new DSL: `compileSdk = 36`, `minSdk = 26`, `targetSdk = 36`, `kotlin { jvmToolchain(21) }`, `optimization { enable = false }` for debug, release block with `isMinifyEnabled = true`, `isShrinkResources = true`, `proguardFiles("proguard-rules.pro")`, and verify `./gradlew.bat :app:assembleDebug` succeeds
- [x] 1.6 Create `app/proguard-rules.pro` with keep rules for `kotlinx.serialization` serializers (`-keep class kotlinx.serialization.** { *; }`, `-keep @kotlinx.serialization.Serializable class *`), and verify release build doesn't strip serializers
- [x] 1.7 Configure `baselineprofile/build.gradle.kts` with `androidx.baselineprofile` plugin, `androidx.benchmark:benchmark-macro-junit4` dependency, `targetProjectPath = ":app"`, and verify module configures without errors

## 2. Directory Structure & Core Infrastructure

- [x] 2.1 Create package directory structure: `app/src/main/java/com/template/app/{di,navigation,ui/theme,presentation/home,domain/{model,repository,usecase},data/{repository,datasource,dto,mapper}}` and verify directories exist
- [x] 2.2 Create `TemplateApp.kt` (Application class) with `AppContainer` instantiation in `onCreate()`, and verify app compiles with Application registered in manifest
- [x] 2.3 Create `di/AppContainer.kt` with lazy singletons for: `ExampleLocalDataSource`, `ExampleRepositoryImpl`, `GetExampleItemsUseCase`, `HomeViewModel` factory, and verify all dependencies resolve without circular references
- [x] 2.4 Create `navigation/Routes.kt` with `@Serializable` sealed interface `Route` and data objects `Home` and `Detail(val id: Int)`, and verify serialization compiles
- [x] 2.5 Create `navigation/AppNavHost.kt` with `NavHost` using `rememberNavController()`, `composable(route = Routes.Home.route)`, `composable(route = Routes.Detail.route)` with typed argument retrieval, and verify navigation graph builds
- [x] 2.6 Create `ui/theme/Color.kt`, `ui/theme/Type.kt`, `ui/theme/Theme.kt` with Material 3 `MaterialTheme` using `darkColorScheme`/`lightColorScheme`, typography, and verify theme applies in preview

## 3. Domain Layer (Pure Kotlin, Zero Android Imports)

- [x] 3.1 Create `domain/model/ExampleItem.kt` as immutable `data class ExampleItem(val id: Int, val title: String, val description: String)`, and verify no Android imports
- [x] 3.2 Create `domain/repository/ExampleRepository.kt` interface with `suspend fun getItems(): Result<List<ExampleItem>>` (using sealed `Result` class or `kotlin.Result`), and verify interface is pure Kotlin
- [x] 3.3 Create `domain/usecase/GetExampleItemsUseCase.kt` as class with `operator fun invoke(): Result<List<ExampleItem>>` delegating to repository, and verify use case is single-responsibility

## 4. Data Layer (Implementation Details)

- [x] 4.1 Create `data/dto/ExampleItemDto.kt` with `@Serializable data class ExampleItemDto(val id: Int, val title: String, val description: String)`, and verify kotlinx.serialization compiles
- [x] 4.2 Create `data/mapper/ExampleMapper.kt` with `fun ExampleItemDto.toDomain(): ExampleItem` extension, and verify mapping works correctly
- [x] 4.3 Create `data/datasource/ExampleLocalDataSource.kt` with in-memory fake data list and `suspend fun getItems(): List<ExampleItemDto>` returning test data, and verify data source returns expected items
- [x] 4.4 Create `data/repository/ExampleRepositoryImpl.kt` implementing `ExampleRepository`, injecting `ExampleLocalDataSource` and `ExampleMapper`, mapping DTOs to domain models, and verify implementation compiles and satisfies interface

## 5. Presentation Layer (Compose UI + ViewModel)

- [x] 5.1 Create `presentation/home/HomeUiState.kt` as `data class HomeUiState(val isLoading: Boolean = false, val items: List<ExampleItem> = emptyList(), val error: String? = null)`, and verify all properties are `val`
- [x] 5.2 Create `presentation/home/HomeUiEvent.kt` as `sealed interface HomeUiEvent` with `data class LoadItems : HomeUiEvent` and `data class ItemClicked(val item: ExampleItem) : HomeUiEvent`, and verify sealed interface compiles
- [x] 5.3 Create `presentation/home/HomeViewModel.kt` with constructor-injected `GetExampleItemsUseCase`, private `MutableStateFlow<HomeUiState>`, public `val state: StateFlow<HomeUiState>`, `fun onEvent(event: HomeUiEvent)` handling `LoadItems` by calling use case and updating state, and verify ViewModel exposes immutable StateFlow only
- [x] 5.4 Create `presentation/home/HomeScreen.kt` Composable with `Scaffold`, `TopAppBar`, `LazyColumn` of `Card` items showing `ExampleItem.title`/`description`, `LaunchedEffect` triggering `LoadItems` on start, `viewModel.state.collectAsStateWithLifecycle()`, and verify UI renders correctly in preview
- [x] 5.5 Update `MainActivity.kt` to extend `ComponentActivity`, set content to `AppNavHost(container = AppContainer)`, and verify activity launches without crash

## 6. Baseline Profiles

- [ ] 6.1 Create `baselineprofile/src/main/java/com/template/app/baselineprofile/BaselineProfileGenerator.kt` with `@RunWith(AndroidJUnit4::class)` class, `BaselineProfileRule` targeting `TemplateApp`, `collectBaselineProfile` test covering `HomeScreen` startup and scroll, and verify generator compiles
- [ ] 6.2 Verify `androidx.baselineprofile` plugin applied to `:app` module generates `src/main/baseline-prof.txt` when running `./gradlew.bat :baselineprofile:connectedCheck` (requires device/emulator)
- [ ] 6.3 Verify release build (`./gradlew.bat :app:assembleRelease`) includes baseline profile in APK/AAB

## 7. End-to-End Verification

- [x] 7.1 Run `./gradlew.bat assembleDebug` and verify debug APK builds successfully with no errors
- [x] 7.2 Run `./gradlew.bat assembleRelease` and verify release APK builds successfully with minification, shrinking, R8 full mode
- [ ] 7.3 Launch app on emulator/device and verify `HomeScreen` displays list of ExampleItems with Material 3 styling
- [ ] 7.4 Verify navigation to `Detail` route works (placeholder text visible) demonstrating type-safe argument passing
- [ ] 7.5 Verify all architectural layers are exercised: HomeScreen → HomeViewModel.onEvent → GetExampleItemsUseCase → ExampleRepositoryImpl → ExampleLocalDataSource → ExampleItemDto → ExampleMapper → ExampleItem → HomeUiState → recomposition