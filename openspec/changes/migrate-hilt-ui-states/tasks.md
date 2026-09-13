## 1. Hilt & Gradle Setup

- [ ] 1.1 Add Hilt and Kapt plugins to `gradle/libs.versions.toml`, `build.gradle.kts`, and `app/build.gradle.kts` and verify configuration by running `.\gradlew.bat assembleDebug`
- [ ] 1.2 Add `androidx.hilt:hilt-navigation-compose` dependency to `app/build.gradle.kts` for Compose ViewModel injection

## 2. Dependency Injection & Mapper Refactoring

- [ ] 2.1 Refactor `ExampleMapper` into an injectable `@Inject constructor()` class and update `ExampleRepositoryImpl` constructor to take `ExampleMapper` via `@Inject`
- [ ] 2.2 Create Hilt `DataModule` with `@Binds` for `ExampleRepository` to `ExampleRepositoryImpl` and `@Inject constructor()` support for `ExampleLocalDataSource` and `GetExampleItemsUseCase`

## 3. Hilt Migration & Cleanup

- [ ] 3.1 Annotate `TemplateApp` with `@HiltAndroidApp` and `MainActivity` with `@AndroidEntryPoint`
- [ ] 3.2 Delete manual `AppContainer` and remove container references from `MainActivity` and `AppNavHost`
- [ ] 3.3 Annotate `HomeViewModel` with `@HiltViewModel` and `@Inject constructor(...)`, and update `HomeScreen` to retrieve ViewModel via `hiltViewModel()`

## 4. UI States & Screen Implementation

- [ ] 4.1 Update `HomeUiState` to model Loading, Error, Empty, and Success states cleanly
- [ ] 4.2 Update `HomeScreen` and screen content composables to render dedicated UI components for Loading, Error (with Retry button), Empty, and Content states
- [ ] 4.3 Verify the entire application compiles cleanly with `.\gradlew.bat assembleDebug`
