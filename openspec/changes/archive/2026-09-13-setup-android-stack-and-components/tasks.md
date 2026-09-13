## 1. Gradle & Dependency Catalog Configuration

- [x] Add Room (with KSP), Ktor 3, Coil 3, and Navigation Compose versions to `gradle/libs.versions.toml`
- [x] Add plugin definitions for `kotlin-ksp` and `room` in `gradle/libs.versions.toml`
- [x] Apply `kotlin-ksp`, `hilt-android`, and `kotlin-serialization` in `build.gradle.kts` and `app/build.gradle.kts`
- [x] Add Room runtime, Room KSP compiler, Ktor Android engine, Ktor serialization, Ktor logging, Ktor timeouts, Coil 3 Compose, and Navigation Compose dependencies to `app/build.gradle.kts`

## 2. Hilt Dependency Injection Modules

- [x] Create `AppModule.kt` under `com.template.app.di` providing `@ApplicationContext` bindings
- [x] Create `NetworkModule.kt` under `com.template.app.di` providing configured `HttpClient` singleton (Ktor engine, JSON ContentNegotiation, logging, timeouts)
- [x] Create `DatabaseModule.kt` under `com.template.app.di` providing `AppDatabase` and `ItemDao` singletons

## 3. Local Data Storage Layer (Room + Flow)

- [x] Create `ItemEntity.kt` under `com.template.app.data.local` annotated with `@Entity`
- [x] Create `ItemDao.kt` under `com.template.app.data.local` with `@Query`, `@Insert`, `@Update`, `@Delete` operations returning `Flow<List<ItemEntity>>` and `suspend` methods
- [x] Create `AppDatabase.kt` under `com.template.app.data.local` extending `RoomDatabase`
- [x] Create `ItemRepository.kt` and `ItemRepositoryImpl.kt` under `com.template.app.data.repository` bridging DAO and RemoteDataSource using `Flow`

## 4. Remote Network Layer (Ktor Client)

- [x] Create API DTO models (`ItemDto.kt`) under `com.template.app.data.remote` annotated with `@Serializable`
- [x] Create `ApiService.kt` interface and implementation under `com.template.app.data.remote` using Ktor `HttpClient`
- [x] Create `RemoteDataSource.kt` under `com.template.app.data.remote` for robust remote error handling and API response mapping

## 5. Type-Safe Navigation Compose Graph

- [x] Define type-safe route destinations (`HomeRoute`, `DetailsRoute`, `ProfileRoute`) under `com.template.app.ui.navigation`
- [x] Implement `AppNavGraph.kt` composable using `NavHost` and `composable<Route>` with arguments
- [x] Implement `HomeScreen.kt`, `DetailsScreen.kt`, and `ProfileScreen.kt` under `com.template.app.ui.screens`

## 6. Reusable Material 3 Compose & Coil Components

- [x] Implement `AppButton.kt`, `AppTextField.kt`, `AppCard.kt`, `AppTopBar.kt`, and `AppDialog.kt` under `com.template.app.ui.components`
- [x] Implement `LoadingState.kt`, `ErrorState.kt`, and `EmptyState.kt` feedback composables under `com.template.app.ui.components`
- [x] Implement `AppImage.kt` using Coil 3 Compose with placeholder, crossfade, and error handlers under `com.template.app.ui.components`

## 7. Verification & Build Validation

- [x] Run `.\gradlew.bat assembleDebug` to verify complete clean compilation without errors
- [x] Run `.\gradlew.bat testDebugUnitTest` to verify tests pass cleanly
