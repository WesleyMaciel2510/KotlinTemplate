## Context

See proposal.md for motivation. The existing project uses:
- Kotlin + Jetpack Compose + Material 3
- MVVM + Clean Architecture with Hilt DI
- Type-safe Navigation Compose with serializable routes
- CameraX already in version catalog (v1.4.1)
- ML Kit Barcode Scanning already in version catalog (v17.3.0)
- Gradle version catalog in `gradle/libs.versions.toml`

## Goals / Non-Goals

**Goals:**
- Implement QR code scanner screen with CameraX + ML Kit
- Spotlight frame overlay (280dp square, rounded corners, white border, 50% opacity outside)
- Single QR code scan with debounce and dialog confirmation
- Camera permission handling with retry
- Portrait orientation, 60fps preview
- Type-safe navigation integration

**Non-Goals:**
- Support for other barcode formats (EAN, UPC, etc.)
- Landscape orientation
- Flashlight/torch control
- Gallery image picker for QR codes
- Scan history/persistence

## Decisions

### 1. CameraX for Preview and Analysis
**Decision**: Use CameraX `PreviewView` for camera preview and `ImageAnalysis` for barcode detection.
**Rationale**: CameraX is already in the project, provides lifecycle-aware camera management, handles rotation, and integrates with Compose via `AndroidView`. ML Kit's `InputImage.fromMediaImage()` works directly with CameraX's `ImageProxy`.
**Alternatives**: Camera2 API directly (more boilerplate, no lifecycle management), third-party scanner libraries (adds dependencies, less control).

### 2. ML Kit Barcode Scanning for QR Detection
**Decision**: Use ML Kit's `BarcodeScanning` client with `Barcode.FORMAT_QR_CODE` only.
**Rationale**: Already in version catalog, on-device, fast, supports QR codes specifically. Configure `BarcodeScannerOptions` with `setBarcodeFormats(Barcode.FORMAT_QR_CODE)`.
**Alternatives**: ZXing/ZBar (larger APK, more complex integration), ML Kit's `BarcodeScanner` (deprecated in favor of `BarcodeScanning`).

### 3. Spotlight Frame Implementation
**Decision**: Custom Compose canvas drawing over `PreviewView` in a `Box` - draw semi-transparent overlay with a clear rectangular hole (280dp, 16dp corner radius).
**Rationale**: Pure Compose, no custom View needed, easy to animate if needed, stays in sync with preview size.
**Alternatives**: Custom View with PorterDuff.Mode.CLEAR (more complex, not Compose-native).

### 4. Debounce and Single Scan
**Decision**: Use a debounce mechanism in the analyzer (2-second window) + pause scanning via `ImageAnalysis.clearAnalyzer()` when dialog opens, resume via `setAnalyzer()` after dialog closes.
**Rationale**: Prevents duplicate scans, ML Kit's `process()` is async, pausing analyzer is cleaner than boolean flags.
**Alternatives**: Boolean flag in analyzer (race conditions possible), collect latest only (doesn't pause camera).

### 5. Camera Permission Handling
**Decision**: Use `accompanist-permissions` (already in project) with `rememberPermissionState()` and `PermissionRequired` composable.
**Rationale**: Already in version catalog, Compose-native, handles rationale and settings redirect.
**Alternatives**: ActivityResultContracts (more boilerplate, not Compose-native), custom permission handling.

### 6. URL Confirmation Dialog
**Decision**: Material 3 `AlertDialog` with custom message and buttons.
**Rationale**: Material 3 is the design system, `AlertDialog` is the standard modal component.
**Alternatives**: Custom dialog composable (more code, no benefit).

### 7. Navigation Integration
**Decision**: Add `QrCodeRoute` as a `@Serializable data object` to existing `Routes.kt`, handle in `AppNavHost.kt` composable.
**Rationale**: Follows existing pattern for `HomeRoute`, `ProfileRoute`, `DetailsRoute`.

## Risks / Trade-offs

| Risk | Mitigation |
|------|------------|
| Camera permission denied permanently | Show settings redirect button in error state using `Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)` |
| ML Kit scanning performance on low-end devices | Use `ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST` to drop frames, configure analysis resolution to 720p |
| CameraX lifecycle issues with Compose | Bind to `viewLifecycleOwner` via `LocalLifecycleOwner.current`, use `ProcessCameraProvider` correctly |
| Dialog state causing navigation issues | Use `navController.popBackStack()` in both dialog button callbacks, ensure single navigation action |
| 60fps preview with analysis | Use `setTargetResolution()` on both preview and analysis, avoid heavy processing on main thread |

## Migration Plan

1. Add CameraX and ML Kit dependencies to `gradle/libs.versions.toml` (already present, verify versions)
2. Add dependencies to `app/build.gradle.kts`
3. Create `QrCodeReaderScreen.kt`, `CameraPreview.kt`, `BarcodeAnalyzer.kt` in `com.template.app.qrcode`
4. Add `QrCodeRoute` to `Routes.kt`
5. Add composable route in `AppNavHost.kt`
6. Update `AndroidManifest.xml` with camera permission (already present)
7. Build and test on device/emulator with camera

## Open Questions

- Should the scanner support both front and back cameras? (Default to back camera for now)
- Should there be haptic feedback on successful scan? (Can add later as enhancement)