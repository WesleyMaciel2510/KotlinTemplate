## Why

The application currently lacks a QR code scanning capability. Users need to scan QR codes to quickly navigate to URLs without manual typing. This feature will provide a production-ready QR code scanner screen using CameraX and ML Kit Barcode Scanning, integrated with the existing MVVM + Clean Architecture.

## What Changes

- Add CameraX and ML Kit Barcode Scanning dependencies to the version catalog and app module
- Create a new `QrCodeReaderScreen` composable with full-screen camera preview and spotlight scanning frame
- Implement QR code detection logic with debounce to prevent duplicate scans
- Add camera permission handling with automatic request and error state for denied permission
- Create Material 3 modal dialog for URL confirmation with "NÃO" and "SIM" actions
- Navigate to URL via `ACTION_VIEW` intent on confirmation, or navigate back on cancel
- Integrate the new screen into the existing type-safe navigation graph

## Capabilities

### New Capabilities
- `qr-code-scanner`: Full-screen QR code scanning with CameraX + ML Kit, spotlight frame overlay, permission handling, and URL confirmation dialog

### Modified Capabilities
- `navigation`: Add `QrCodeRoute` to the type-safe navigation graph for accessing the scanner screen

## Impact

- **Dependencies**: CameraX (core, camera2, lifecycle, view), ML Kit Barcode Scanning
- **Code**: New screen composable, analyzer class, camera preview composable, navigation route
- **Permissions**: Camera permission required (added to manifest)
- **Architecture**: Follows existing MVVM + Clean Architecture pattern with Hilt DI