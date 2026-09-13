## Purpose

Provides a production-ready QR code scanning screen using CameraX and ML Kit Barcode Scanning with a spotlight frame overlay, camera permission handling, and URL confirmation dialog.

## ADDED Requirements

### Requirement: Camera Preview with Spotlight Frame
The QR code scanner SHALL display a full-screen camera preview with a centered square scanning frame (~280dp) with rounded corners and a white border. The area outside the square must be covered by a semi-transparent gray overlay (~50% opacity) while the square itself remains completely clear.

#### Scenario: Camera preview renders correctly
- **WHEN** the QR code scanner screen appears
- **THEN** the camera preview fills the screen with a centered 280dp square frame and spotlight overlay

#### Scenario: Spotlight overlay maintains visibility
- **WHEN** the camera is active
- **THEN** content outside the scanning frame is visible but dimmed at ~50% opacity

### Requirement: Automatic Camera Initialization
The camera SHALL start automatically when the screen appears without requiring user interaction to begin scanning.

#### Scenario: Camera starts on screen entry
- **WHEN** the QR code scanner screen is composed
- **THEN** CameraX binds the preview and analysis use cases and starts the camera

### Requirement: QR Code Detection
The scanner SHALL detect QR codes only (not other barcode formats) using ML Kit Barcode Scanning when a QR code enters the scanning frame.

#### Scenario: QR code detected in frame
- **WHEN** a QR code is positioned within the scanning frame
- **THEN** ML Kit processes the frame and returns the decoded QR code value

#### Scenario: Non-QR barcodes ignored
- **WHEN** other barcode formats (EAN, UPC, etc.) enter the frame
- **THEN** they are not detected or returned

### Requirement: Single Scan with Debounce
The scanner SHALL read a QR code once, pause scanning immediately, and prevent duplicate scans while the confirmation dialog is open.

#### Scenario: Scan pauses after detection
- **WHEN** a QR code is successfully decoded
- **THEN** scanning pauses and no further detections are processed until resumed

#### Scenario: Duplicate scans prevented during dialog
- **WHEN** the confirmation dialog is open
- **THEN** additional QR codes in the frame are ignored

### Requirement: URL Confirmation Dialog
Upon QR code detection, a Material 3 modal dialog SHALL display with the exact text "Você deseja ir para '$url'?" with "NÃO" and "SIM" buttons.

#### Scenario: Dialog shows detected URL
- **WHEN** a QR code containing a URL is detected
- **THEN** a modal dialog appears with the exact confirmation text and both buttons

#### Scenario: NÃO button behavior
- **WHEN** user taps "NÃO"
- **THEN** the dialog closes, scanner stops, and navigation returns to previous screen

#### Scenario: SIM button behavior
- **WHEN** user taps "SIM"
- **THEN** the detected URL opens in the device's default browser via ACTION_VIEW intent, then navigation returns to previous screen

### Requirement: Camera Permission Handling
The scanner SHALL request camera permission automatically; if denied, show an error state with a retry button.

#### Scenario: Permission granted
- **WHEN** camera permission is granted
- **THEN** camera preview starts normally

#### Scenario: Permission denied
- **WHEN** camera permission is denied
- **THEN** an error state displays with a retry button to request permission again

#### Scenario: Permission permanently denied
- **WHEN** camera permission is permanently denied (don't ask again)
- **THEN** error state directs user to app settings to enable permission

### Requirement: Portrait Orientation Support
The scanner SHALL support portrait orientation and maintain 60fps camera preview performance.

#### Scenario: Portrait orientation maintained
- **WHEN** device is rotated
- **THEN** the scanner remains in portrait orientation

#### Scenario: Smooth camera preview
- **WHEN** camera is active
- **THEN** preview maintains 60fps without visible lag or frame drops

### Requirement: Integration with Type-Safe Navigation
The QR code scanner screen SHALL be accessible via type-safe navigation route.

#### Scenario: Navigate to scanner screen
- **WHEN** navigation triggers QrCodeRoute
- **THEN** the QR code scanner screen is displayed