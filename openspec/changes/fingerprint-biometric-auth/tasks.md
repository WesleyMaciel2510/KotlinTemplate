## 1. Setup and Dependencies

- [x] 1.1 Add `androidx.biometric:biometric` and `androidx.biometric:biometric-ktx` to `gradle/libs.versions.toml` and verify file syntax is correct
- [x] 1.2 Apply biometric dependencies in `app/build.gradle.kts` and verify project sync/build succeeds with `.\gradlew.bat assembleDebug`
- [x] 1.3 Add Portuguese UI strings for biometric authentication to `app/src/main/res/values/strings.xml` and verify resource compilation

## 2. Security Infrastructure and Domain Layer

- [ ] 2.1 Create `BiometricAuthResult` and `UnavailableReason` sealed classes/enums in `domain/security` and verify they match design.md
- [ ] 2.2 Create `BiometricAuthenticator` interface in `domain/security` and verify it contains availability check and authenticate suspend functions
- [ ] 2.3 Create `AndroidBiometricAuthenticator` in `data/security` with `BiometricPrompt` implementation and verify it maps Android error codes to `BiometricAuthResult`
- [ ] 2.4 Create `SecurityPreferences` interface and `EncryptedSecurityPreferences` implementation using `EncryptedSharedPreferences` for `biometricLoginEnabled` and verify secure storage initialization
- [ ] 2.5 Create `SecurityModule` in `di/` to provide `BiometricAuthenticator` and `SecurityPreferences` via Hilt and verify Hilt bindings are correct

## 3. Presentation Layer

- [ ] 3.1 Create `BiometricAuthState` sealed interface for UI state as defined in design.md
- [ ] 3.2 Create `BiometricAuthViewModel` in `presentation/auth` and verify it handles availability checks, authentication triggering, and state transitions
- [ ] 3.3 Implement duplicate prompt prevention in `BiometricAuthViewModel` and verify it prevents re-entry during authentication
- [ ] 3.4 Add unit tests for `BiometricAuthViewModel` mocking `BiometricAuthenticator` and verify all state transitions (success, cancel, lockout, error) are covered

## 4. UI and Navigation

- [ ] 4.1 Add `BiometricAuthRoute` to `ui/navigation/Routes.kt` and verify it integrates with existing type-safe navigation
- [ ] 4.2 Create `BiometricAuthScreen` in `ui/auth` using Material 3 components and verify it displays title, icon, and button as per specs
- [ ] 4.3 Integrate `BiometricAuthScreen` in `AppNavHost` and verify navigation from/to biometric screen works as expected
- [ ] 4.4 Add UI tests for `BiometricAuthScreen` using `compose-test-junit4` and verify loading, success, and error states are rendered correctly

## 5. Final Integration and Verification

- [ ] 5.1 Run full project build and verify no compilation errors with `.\gradlew.bat assembleDebug`
- [ ] 5.2 Run unit tests and verify all pass with `.\gradlew.bat testDebugUnitTest`
- [ ] 5.3 Verify no sensitive data is logged and no fingerprints are stored by reviewing implementation files
- [ ] 5.4 Verify Portuguese UI text consistency and accessibility content descriptions across the biometric flow