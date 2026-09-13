## Context

The project is a Kotlin Android financial application using:
- Jetpack Compose + Material 3 for UI
- MVVM architecture with Hilt for DI
- Navigation Compose for routing
- Room for local persistence
- Ktor for networking
- Version catalog (`gradle/libs.versions.toml`) for dependency management
- Minimum SDK 26, Target SDK 37, Compile SDK 37
- Java 11 source/target compatibility

Current architecture has:
- `di/` modules: `AppModule`, `DataModule`, `DatabaseModule`, `NetworkModule`
- `domain/` layer with use cases and repository interfaces
- `data/` layer with repository implementations, Room database, Ktor API
- `presentation/` layer with ViewModels and UI state/events
- `ui/` layer with Compose screens and components
- `navigation/` with type-safe routes using `@Serializable` sealed interfaces

No existing authentication or secure storage mechanism exists.

## Goals / Non-Goals

**Goals:**
- Implement fingerprint biometric authentication using AndroidX Biometric library with `BIOMETRIC_STRONG`
- Create reusable `BiometricAuthenticator` abstraction (domain) and `AndroidBiometricAuthenticator` (data)
- Integrate with Hilt DI via new `SecurityModule`
- Create `BiometricAuthViewModel` with typed `StateFlow` authentication states
- Create `BiometricAuthScreen` Compose UI with Material 3
- Add biometric route to Navigation Compose
- Implement secure storage for `biometricLoginEnabled` preference using Android Keystore-backed `EncryptedSharedPreferences`
- Handle all biometric error codes mapped to application-level states
- Prevent duplicate authentication prompts
- Support Portuguese UI text
- Ensure accessibility compliance
- Add unit and UI tests

**Non-Goals:**
- Face ID / face recognition implementation (explicitly excluded)
- Fingerprint enrollment UI (delegated to system settings)
- Custom cryptography (use Android Keystore if needed)
- Backend authentication integration (separate concern)
- Device PIN/pattern/password fallback (unless product requirements change)

## Decisions

### 1. Biometric Library Version

**Decision**: Use `androidx.biometric:biometric:1.2.0-alpha04` (or latest stable) and `androidx.biometric:biometric-ktx` for Kotlin coroutines support.

**Rationale**: Latest AndroidX Biometric provides `BiometricPrompt` with coroutine extensions (`authenticate()` suspend function), proper `BIOMETRIC_STRONG` support, and handles device/OS variations.

**Alternatives considered**:
- Legacy `FingerprintManager` (deprecated since API 28, no strong biometric support)
- `androidx.biometric:biometric:1.1.0` (stable but lacks coroutine API)

### 2. Architecture Layering

**Decision**: Follow the existing domain/data separation:
- `domain/security/BiometricAuthenticator` interface
- `data/security/AndroidBiometricAuthenticator` implementation
- `di/SecurityModule` for Hilt bindings
- `presentation/auth/BiometricAuthViewModel`
- `ui/auth/BiometricAuthScreen`

**Rationale**: Matches existing pattern (e.g., `ExampleRepository` / `ExampleRepositoryImpl`). Keeps ViewModel framework-agnostic and testable.

**Alternatives considered**:
- Calling `BiometricPrompt` directly from ViewModel (violates separation, hard to test)
- Putting biometric logic in UseCase (over-engineering for this scope)

### 3. Authentication State Representation

**Decision**: Use a sealed interface `BiometricAuthState` with typed states:
```kotlin
sealed interface BiometricAuthState {
    data object CheckingAvailability : BiometricAuthState
    data object Available : BiometricAuthState
    data object Authenticating : BiometricAuthState
    data object Authenticated : BiometricAuthState
    data object Canceled : BiometricAuthState
    data class Unavailable(val reason: UnavailableReason) : BiometricAuthState
    data object NotEnrolled : BiometricAuthState
    data object LockedOut : BiometricAuthState
    data object PermanentLockout : BiometricAuthState
    data class Error(val message: String) : BiometricAuthState
}

enum class UnavailableReason { NoHardware, HardwareUnavailable, DevicePolicy, Unknown }
```

**Rationale**: Type-safe, exhaustive when handling in Compose, maps cleanly from Android biometric error codes, supports UI state rendering.

**Alternatives considered**:
- String-based states (not type-safe)
- Single state class with optional fields (less explicit)

### 4. Biometric Error Code Mapping

**Decision**: Map Android `BiometricPrompt.AuthenticationError` codes to application-level `BiometricAuthResult` sealed class in the data layer:

| Android Error | Application Result |
|---------------|-------------------|
| `BIOMETRIC_SUCCESS` | `Success` |
| `BIOMETRIC_ERROR_USER_CANCELED` | `Canceled` |
| `BIOMETRIC_ERROR_NEGATIVE_BUTTON` | `Canceled` |
| `BIOMETRIC_ERROR_NO_HARDWARE` | `NotAvailable(NoHardware)` |
| `BIOMETRIC_ERROR_HW_UNAVAILABLE` | `NotAvailable(HardwareUnavailable)` |
| `BIOMETRIC_ERROR_NONE_ENROLLED` | `NotEnrolled` |
| `BIOMETRIC_ERROR_LOCKOUT` | `LockedOut` |
| `BIOMETRIC_ERROR_LOCKOUT_PERMANENT` | `PermanentLockout` |
| `BIOMETRIC_ERROR_TIMEOUT` | `Error("Timeout")` |
| `BIOMETRIC_ERROR_NO_DEVICE_CREDENTIAL` | `NotAvailable(DevicePolicy)` |
| Others | `Error("Unknown")` |

**Rationale**: Keeps Android framework details out of ViewModel/UI. Single mapping location in `AndroidBiometricAuthenticator`.

### 5. Secure Storage for Preference

**Decision**: Use `EncryptedSharedPreferences` backed by Android Keystore (MasterKeys) for `biometricLoginEnabled` preference.

**Rationale**: 
- Project has no existing secure storage
- `EncryptedSharedPreferences` is the recommended Android solution for sensitive preferences
- Keys managed by Android Keystore (hardware-backed when available)
- Simple API, no additional dependencies

**Alternatives considered**:
- Plain `SharedPreferences` (insufficient for authentication state)
- `Jetpack Security` library `EncryptedFile` (overkill for boolean preference)
- `DataStore` with encryption (more complex, EncryptedSharedPreferences sufficient)

### 6. Duplicate Prompt Prevention

**Decision**: Use `LaunchedEffect(Unit)` in ViewModel to trigger authentication once per screen lifecycle, combined with a `_isAuthenticating` flag in ViewModel to prevent re-entry.

**Rationale**: 
- `LaunchedEffect(Unit)` runs once per composition lifecycle
- ViewModel survives configuration changes
- Flag prevents multiple clicks during authentication
- Clean separation: ViewModel owns authentication state machine

**Alternatives considered**:
- `DisposableEffect` for cleanup (works but more verbose)
- `remember` with key in Compose (doesn't survive config changes)

### 7. Navigation Integration

**Decision**: Add `BiometricAuthRoute` to existing `@Serializable` sealed route hierarchy in `ui.navigation.Routes`. Navigate from login/launch to `BiometricAuthRoute`, then to `HomeRoute` on success.

**Rationale**: Consistent with existing type-safe navigation pattern. No changes to navigation infrastructure needed.

### 8. Biometric Prompt Configuration

**Decision**: Configure `BiometricPrompt.PromptInfo` with:
- Title: "Autenticação por impressão digital"
- Subtitle: "Confirme sua identidade para continuar"
- Negative button: "Cancelar"
- `setAllowedAuthenticators(BIOMETRIC_STRONG)`
- No device credential fallback (`setDeviceCredentialAllowed(false)`)

**Rationale**: Matches requirements for fingerprint-only, Portuguese UI, no PIN/pattern fallback.

### 9. First-Time Setup / No Enrollment Handling

**Decision**: When `BIOMETRIC_ERROR_NONE_ENROLLED`, show screen with "Nenhuma impressão digital cadastrada" message and button to open system biometric settings via `Settings.ACTION_BIOMETRIC_ENROLL`.

**Rationale**: Android does not allow in-app enrollment. Directing to system settings is standard pattern.

### 10. Lockout Handling

**Decision**: 
- Temporary lockout (`BIOMETRIC_ERROR_LOCKOUT`): Show message "Autenticação por impressão digital temporariamente indisponível. Tente novamente mais tarde." with fallback to normal login.
- Permanent lockout (`BIOMETRIC_ERROR_LOCKOUT_PERMANENT`): Show message "Impressão digital bloqueada permanentemente. Use as configurações do dispositivo para desbloquear." and disable biometric login preference.

**Rationale**: Prevents repeated failing prompts. Guides user to correct remediation.

## Risks / Trade-offs

| Risk | Mitigation |
|------|------------|
| BiometricPrompt behavior varies across OEMs/OS versions | Use latest biometric-ktx, test on multiple API levels (26-37), handle unknown errors gracefully |
| EncryptedSharedPreferences requires API 23+ (minSdk 26 OK) | Verified minSdk 26 supports it |
| No existing session/token architecture to integrate with | Design biometric as local verification only; document integration points for future auth system |
| Configuration cache enabled in Gradle | Avoid task-time `project` access in build logic |
| Hilt KSP processing with new module | Add SecurityModule to existing Hilt component structure |

## Migration Plan

1. Add biometric dependencies to `libs.versions.toml` and `app/build.gradle.kts`
2. Create domain/data/di/presentation/ui files
3. Add navigation route and integrate in `AppNavHost`
4. Add strings and UI resources
5. Write unit tests for ViewModel and authenticator
6. Write UI tests for BiometricAuthScreen
7. Build and verify (`./gradlew.bat assembleDebug`)
8. Run tests (`./gradlew.bat testDebugUnitTest`)

## Open Questions

- Should `BiometricAuthenticator` interface expose `suspend fun authenticate(): BiometricAuthResult` or use callback/Flow? (Decision: suspend function with biometric-ktx)
- Exact secure storage implementation details for `EncryptedSharedPreferences` initialization? (Resolve during implementation)
- Integration point with future backend authentication? (Document in code, implement when auth system exists)