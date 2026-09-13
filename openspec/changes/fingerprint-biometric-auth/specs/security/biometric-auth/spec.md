## Purpose

Provides fingerprint biometric authentication for secure access to the financial application, including availability checking, authentication prompt display, result handling, enable/disable preference management, and integration with application navigation and session architecture.

## ADDED Requirements

### Requirement: Biometric hardware availability detection

The system SHALL detect whether the device has biometric hardware capable of strong authentication before offering fingerprint authentication to the user.

#### Scenario: Hardware available and capable
- **WHEN** the system checks biometric availability
- **THEN** the system SHALL report that strong biometric authentication is available if the device has fingerprint hardware enrolled with at least one fingerprint

#### Scenario: No biometric hardware
- **WHEN** the system checks biometric availability on a device without fingerprint sensor
- **THEN** the system SHALL report `BIOMETRIC_ERROR_NO_HARDWARE` and fingerprint authentication SHALL NOT be offered

#### Scenario: Hardware present but not strong
- **WHEN** the system checks biometric availability on a device with only weak biometric (e.g., face recognition without IR)
- **THEN** the system SHALL report that strong biometric authentication is NOT available and fingerprint authentication SHALL NOT be offered

### Requirement: Biometric enrollment status detection

The system SHALL detect whether at least one fingerprint is enrolled on the device before offering fingerprint authentication.

#### Scenario: Fingerprint enrolled
- **WHEN** the system checks enrollment status and at least one fingerprint is enrolled
- **THEN** the system SHALL allow fingerprint authentication to proceed

#### Scenario: No fingerprint enrolled
- **WHEN** the system checks enrollment status and no fingerprints are enrolled
- **THEN** the system SHALL report `BIOMETRIC_ERROR_NONE_ENROLLED` and display a clear message directing the user to enroll a fingerprint in system settings

### Requirement: Biometric authentication prompt display

The system SHALL display the Android system biometric authentication prompt with fingerprint-specific messaging when the user initiates fingerprint authentication.

#### Scenario: Prompt displayed with correct content
- **WHEN** the user taps the fingerprint authentication button
- **THEN** the system SHALL display the system biometric prompt with title "Autenticação por impressão digital" and subtitle "Confirme sua identidade para continuar"

#### Scenario: Prompt uses BIOMETRIC_STRONG
- **WHEN** the biometric prompt is created
- **THEN** the system SHALL configure it with `BIOMETRIC_STRONG` authentication strength

#### Scenario: Prompt includes negative button
- **WHEN** the biometric prompt is displayed
- **THEN** the system SHALL include a negative/cancel button labeled appropriately for the flow (e.g., "Cancelar")

### Requirement: Biometric authentication result handling

The system SHALL handle all biometric authentication results and map them to typed application-level states.

#### Scenario: Authentication success
- **WHEN** the user successfully authenticates with fingerprint
- **THEN** the system SHALL return a typed `Success` result and update authentication state to `Authenticated`

#### Scenario: User cancellation
- **WHEN** the user taps the cancel/negative button
- **THEN** the system SHALL return a typed `Canceled` result and NOT navigate to protected content

#### Scenario: Authentication timeout
- **WHEN** the biometric prompt times out
- **THEN** the system SHALL return a typed `Error` result with `BIOMETRIC_ERROR_TIMEOUT` and allow retry

#### Scenario: Temporary lockout
- **WHEN** the biometric subsystem reports temporary lockout (too many failed attempts)
- **THEN** the system SHALL return a typed `LockedOut` result, explain temporary unavailability, and allow fallback to normal authentication

#### Scenario: Permanent lockout
- **WHEN** the biometric subsystem reports permanent lockout
- **THEN** the system SHALL return a typed `PermanentLockout` result and require device credential to restore biometric availability

#### Scenario: Hardware unavailable
- **WHEN** the biometric hardware becomes temporarily unavailable
- **THEN** the system SHALL return a typed `HardwareUnavailable` result and allow fallback authentication

#### Scenario: Unknown error
- **WHEN** an unexpected biometric error occurs
- **THEN** the system SHALL return a typed `UnknownError` result with the error code for logging and allow retry

### Requirement: Biometric authentication duplicate prevention

The system SHALL prevent duplicate biometric authentication prompts from being triggered by recomposition, configuration changes, lifecycle events, navigation recreation, multiple clicks, or process state changes.

#### Scenario: Single prompt per user action
- **WHEN** the user taps the fingerprint button multiple times rapidly
- **THEN** only one biometric prompt SHALL be displayed

#### Scenario: No prompt on recomposition
- **WHEN** the biometric screen recomposes during authentication
- **THEN** the system SHALL NOT display a new biometric prompt

#### Scenario: No prompt on configuration change
- **WHEN** device rotation occurs during authentication
- **THEN** the system SHALL maintain the authentication state and NOT display a new prompt

### Requirement: Biometric enablement preference

The system SHALL provide a secure application-level preference indicating whether the user has chosen to use fingerprint authentication.

#### Scenario: Enable biometric login after successful authentication
- **WHEN** the user enables fingerprint login and successfully authenticates
- **THEN** the system SHALL persist `biometricLoginEnabled = true` using secure storage

#### Scenario: Disable biometric login with authentication
- **WHEN** the user disables fingerprint login after appropriate authentication
- **THEN** the system SHALL persist `biometricLoginEnabled = false` using secure storage

#### Scenario: Preference not enabled by default
- **WHEN** the application is installed or biometric hardware becomes available
- **THEN** `biometricLoginEnabled` SHALL default to `false` and NOT be enabled merely because hardware exists

#### Scenario: Secure storage used
- **WHEN** the biometric enablement preference is persisted
- **THEN** the system SHALL use the project's secure storage mechanism (not plain SharedPreferences) for sensitive authentication state

### Requirement: Biometric authentication integration with application session

The system SHALL clearly separate local biometric verification from application session/token validity and backend authentication.

#### Scenario: Biometric success does not validate expired backend session
- **WHEN** biometric authentication succeeds but the backend session/token is expired
- **THEN** the system SHALL NOT consider the user fully authenticated for backend operations and SHALL trigger the existing re-authentication flow

#### Scenario: Biometric success with valid session
- **WHEN** biometric authentication succeeds and the backend session/token is valid
- **THEN** the system SHALL allow access to protected content

### Requirement: Navigation integration

The system SHALL integrate fingerprint authentication with the existing Navigation Compose architecture.

#### Scenario: Login → Biometric → Home flow
- **WHEN** the user launches the app and biometric login is enabled
- **THEN** the system SHALL navigate to the biometric authentication screen, then to Home on success

#### Scenario: App Launch → Biometric → Protected Area flow
- **WHEN** the app is launched and biometric login is enabled with a valid session
- **THEN** the system SHALL navigate to the biometric authentication screen, then to the protected area on success

#### Scenario: Fallback to normal login
- **WHEN** biometric authentication is unavailable, canceled, or fails
- **THEN** the system SHALL allow navigation to the normal login screen

### Requirement: Biometric screen UI

The system SHALL provide a Material 3 biometric authentication screen with fingerprint icon, title, explanation, authentication button, loading state, error state, retry action, and fallback to normal login.

#### Scenario: Screen displays correctly
- **WHEN** the biometric authentication screen is shown
- **THEN** it SHALL display a fingerprint icon, title "Entrar com impressão digital", explanation "Use sua impressão digital para acessar sua conta com segurança.", and button "Usar impressão digital"

#### Scenario: Loading state during authentication
- **WHEN** biometric authentication is in progress
- **THEN** the screen SHALL show a loading/authenticating state and disable the authentication button

#### Scenario: Error state with retry
- **WHEN** biometric authentication fails with a recoverable error
- **THEN** the screen SHALL display an error message and a retry action

#### Scenario: Fallback to normal login
- **WHEN** biometric authentication is unavailable or user chooses not to use it
- **THEN** the screen SHALL provide a fallback action to navigate to normal login

### Requirement: Accessibility support

The biometric authentication screen SHALL meet accessibility requirements.

#### Scenario: TalkBack support
- **WHEN** TalkBack is enabled
- **THEN** all interactive elements SHALL have meaningful content descriptions

#### Scenario: Touch target sizes
- **WHEN** the screen is rendered
- **THEN** all interactive elements SHALL meet minimum touch target sizes (48dp)

#### Scenario: Contrast and font scaling
- **WHEN** the screen is rendered with large font sizes or high contrast
- **THEN** text SHALL remain readable and meet contrast requirements

#### Scenario: No color-only information
- **WHEN** error or success states are displayed
- **THEN** information SHALL NOT be communicated exclusively through color

## MODIFIED Requirements

### Requirement: App navigation routes

The navigation system SHALL include a biometric authentication route.

#### Scenario: Biometric route added
- **WHEN** the navigation graph is built
- **THEN** a `BiometricAuthRoute` SHALL be available for navigation from login/launch to protected areas