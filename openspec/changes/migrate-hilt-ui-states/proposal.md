## Why

The current manual dependency injection (`AppContainer`) contains compilation and wiring flaws around `ExampleMapper` injection and forces manual passing of ViewModel dependencies through `AppNavHost`. Migrating to Hilt provides standardized, compile-time dependency injection and enables `hiltViewModel()` injection in Jetpack Compose screens. Furthermore, the UI lacks explicit loading, error, and empty state representations, creating a sub-optimal user experience when items fail to load or are empty.

## What Changes

- **DI Refactoring & Hilt Migration**: Replace manual `AppContainer` with Hilt dependency injection. Inject `ExampleMapper` into `ExampleRepositoryImpl` and provide DI bindings for repositories, data sources, and use cases using Hilt modules.
- **ViewModel & Compose Hilt Integration**: Annotate `HomeViewModel` with `@HiltViewModel` and update UI screens to use Hilt injection (`hiltViewModel()`) instead of passing manual `AppContainer` instances.
- **Robust UI States**: Introduce structured UI state models and dedicated Composables for Loading, Error (with retry mechanism), Empty, and Success states in UI screens.

## Capabilities

### New Capabilities
- `di-hilt`: Standardized dependency injection setup using Hilt, providing singleton modules for data sources, repositories, mappers, and Hilt ViewModels.
- `ui-states`: Comprehensive UI state handling covering Loading, Error, Empty, and Content/Success states across application screens.

### Modified Capabilities

## Impact

- **Build Configuration**: Configures `hilt-android` and `kotlin-kapt` plugins in `app/build.gradle.kts` and `libs.versions.toml`.
- **Application Class**: Annotates `TemplateApp` with `@HiltAndroidApp`.
- **Activity & Navigation**: Annotates `MainActivity` with `@AndroidEntryPoint` and updates `AppNavHost` to drop manual `AppContainer` dependency.
- **Data & Domain Layer**: Refactor `ExampleMapper` into an injectable component and inject `ExampleRepositoryImpl` via `@Inject`.
- **UI & ViewModels**: Updates `HomeViewModel` and screen composables (`HomeScreen`, `HomeContentScreen`, `FavoritesContentScreen`, etc.) to handle Loading, Error, and Empty states gracefully.
