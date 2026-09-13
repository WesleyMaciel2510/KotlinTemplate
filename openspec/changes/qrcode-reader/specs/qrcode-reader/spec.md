## Purpose

Enables the app to detect and decode QR codes and barcodes in real time using the device camera, delivering decoded values to the host via a callback.

## ADDED Requirements

### Requirement: Camera permission is requested before scanning
The system SHALL request `android.permission.CAMERA` at runtime before activating the camera preview. If permission is denied, scanning SHALL NOT start and the UI SHALL surface a rationale or settings-redirect message.

#### Scenario: Permission granted on first request
- **WHEN** the user opens the QR reader screen for the first time and grants the camera permission
- **THEN** the camera preview starts and barcode detection is active

#### Scenario: Permission permanently denied
- **WHEN** the user has permanently denied the camera permission
- **THEN** the screen SHALL display a message explaining why the permission is needed and provide a link to app settings

### Requirement: Live camera preview with barcode overlay
The system SHALL display a real-time camera preview that fills the available screen area while barcode detection runs continuously on each frame.

#### Scenario: Camera preview is visible
- **WHEN** camera permission is granted and the screen is active
- **THEN** a live camera preview SHALL be rendered and kept in sync with the lifecycle of the composable

#### Scenario: Barcode detected in frame
- **WHEN** ML Kit detects at least one barcode in the current camera frame
- **THEN** the first detected barcode's raw value SHALL be emitted via the `onBarcodeDetected` callback

### Requirement: Supported barcode formats
The system SHALL decode at minimum: QR Code, Data Matrix, PDF417, Aztec, EAN-13, EAN-8, Code 128, Code 39, Code 93, UPC-A, UPC-E, Codabar, and ITF.

#### Scenario: QR code scanned
- **WHEN** a valid QR code is within the camera frame
- **THEN** the decoded string SHALL be delivered to the host within ≤ 1 second of the code entering the frame under normal lighting

### Requirement: Single-result emission (no duplicate callbacks)
After a barcode is detected, the system SHALL suppress further callbacks for the same value for a configurable debounce window (default: 2 seconds) to prevent repeated processing.

#### Scenario: Same QR code stays in frame
- **WHEN** a detected barcode remains visible for more than the debounce window
- **THEN** `onBarcodeDetected` SHALL be called only once per debounce window, not on every frame

### Requirement: Lifecycle-aware camera release
The camera SHALL be released when the composable leaves the composition or the host lifecycle moves to STOPPED to prevent resource leaks.

#### Scenario: Screen navigated away
- **WHEN** the user navigates away from the QR reader screen
- **THEN** the CameraX use cases SHALL be unbound and the camera SHALL be released

### Requirement: Reusable composable API
The QR reader SHALL be exposed as a composable function `QrCodeReaderScreen` accepting at minimum: `modifier: Modifier`, `onBarcodeDetected: (String) -> Unit`, and `onPermissionDenied: () -> Unit`.

#### Scenario: Host receives decoded value
- **WHEN** a barcode is successfully decoded
- **THEN** `onBarcodeDetected` SHALL be invoked with the barcode's raw string value on the main thread
