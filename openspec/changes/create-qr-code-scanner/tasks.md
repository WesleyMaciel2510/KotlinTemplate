## 1. Dependencies & Permissions Configuration

- [x] 1.1 Verify CameraX and ML Kit Barcode Scanning dependencies are declared in `gradle/libs.versions.toml` and applied in `app/build.gradle.kts`
- [x] 1.2 Verify `AndroidManifest.xml` includes `<uses-permission android:name="android.permission.CAMERA" />` and `<uses-feature android:name="android.hardware.camera" android:required="false" />`

## 2. Core Scanning & Camera Components

- [x] 2.1 Implement `BarcodeAnalyzer.kt` under `com.template.app.qrcode` using ML Kit `BarcodeScanning` set to `FORMAT_QR_CODE` with debounce mechanism and verify unit tests pass
- [x] 2.2 Implement `CameraPreview.kt` under `com.template.app.qrcode` wrapping CameraX `PreviewView` inside Compose `AndroidView`
- [x] 2.3 Implement spotlight frame overlay in `QrCodeReaderScreen.kt` using Compose Canvas with 280dp square, rounded corners, white border, and 50% opacity dimmed background

## 3. UI & Dialog Integration

- [x] 3.1 Implement camera permission handling in `QrCodeReaderScreen.kt` using `accompanist-permissions` with error state and settings retry button
- [x] 3.2 Implement Material 3 URL confirmation `AlertDialog` displaying exact text `"Você deseja ir para '$url'?"` with `"NÃO"` and `"SIM"` buttons
- [x] 3.3 Implement button behaviors: `"NÃO"` closes dialog and navigates back; `"SIM"` opens `ACTION_VIEW` intent with URL and navigates back

## 4. Navigation & Wiring

- [x] 4.1 Add `QrCodeRoute` serializable object to `Routes.kt` under `com.template.app.ui.navigation`
- [x] 4.2 Add `composable<QrCodeRoute>` destination in `AppNavHost.kt` and wire navigation callback
- [x] 4.3 Add QR code scanner entry point button to `HomeScreen.kt`

## 5. Verification & Testing

- [x] 5.1 Run `.\gradlew.bat testDebugUnitTest` to verify unit tests pass
- [x] 5.2 Run `.\gradlew.bat assembleDebug` to verify clean compilation without errors
- [x] 5.3 Run app on AVD and verify QR code scanner screen opens, displays camera preview, and renders spotlight overlay