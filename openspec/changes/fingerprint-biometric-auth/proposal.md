## Why

This financial application requires production-ready fingerprint biometric authentication to provide users with a secure, convenient way to access their accounts. Currently, the application has no authentication mechanism. Implementing fingerprint authentication using Android's BiometricPrompt with BIOMETRIC_STRONG will provide strong local authentication while integrating cleanly with the existing Kotlin + Jetpack Compose + Material 3 + MVVM + Hilt architecture.

## What Changes

- Add AndroidX Biometric library dependency via version catalog
- Create `BiometricAuthenticator` domain interface and `AndroidBiometricAuthenticator` implementation
- Add `SecurityModule` for Hilt dependency injection of security components
- Create `BiometricAuthViewModel` with typed application-level authentication states
- Create `BiometricAuthScreen` Compose UI with Material 3 design
- Add biometric authentication routes to Navigation Compose
- Add secure storage for biometric enablement preference (biometricLoginEnabled)
- Add Portuguese UI strings for biometric authentication flow
- Add unit tests for authentication states and ViewModel logic
- Add UI tests for biometric screen rendering and accessibility

## Capabilities

### New Capabilities

- `security/biometric-auth`: Fingerprint biometric authentication capability covering availability checking, authentication prompt, success/cancel/error handling, enable/disable preference, and navigation integration

### Modified Capabilities

- `navigation/app-navigation`: Add biometric authentication route and integrate with existing login/launch flows

## Impact

**Dependencies**: Add `androidx.biometric:biometric` and `androidx.biometric:biometric-ktx` to version catalog and app module
**Code**: New packages under `domain/security`, `data/security`, `di`, `ui/auth`, `presentation/auth`
**Navigation**: New `BiometricAuthRoute` added to sealed route hierarchy
**Resources**: New strings, icons, and theme adjustments for biometric UI
**Security**: No biometric data stored; only enablement preference persisted via secure storage
**Testing**: New unit and UI test files for biometric authentication flow