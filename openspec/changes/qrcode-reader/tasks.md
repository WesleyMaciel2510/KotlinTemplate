## 1. Kotlin Plugin & Gradle Setup

- [x] 1.1 Add `kotlin-android` plugin alias to `gradle/libs.versions.toml` (version compatible with AGP 9.3.2) and verify `.\gradlew.bat assembleDebug` no longer reports "No Kotlin plugin applied"
- [x] 1.2 Apply `alias(libs.plugins.kotlin.android)` in root `build.gradle.kts` (classpath) and in `app/build.gradle.kts` (apply), then verify the build still compiles
- [x] 1.3 Add CameraX (`camerax-core`, `camerax-camera2`, `camerax-lifecycle`, `camerax-view`) and ML Kit (`mlkit-barcode-scanning`) version entries + library aliases to `gradle/libs.versions.toml`, add them to `app/build.gradle.kts` dependencies, and verify `.\gradlew.bat assembleDebug` succeeds with no unresolved dependency errors
- [x] 1.4 Add Compose + Activity-Compose dependencies (if not already present) to the version catalog and `app/build.gradle.kts`; enable `buildFeatures { compose = true }` and set `composeOptions`; verify the app compiles

## 2. Manifest & Permissions

- [x] 2.1 Add `<uses-permission android:name="android.permission.CAMERA" />` and `<uses-feature android:name="android.hardware.camera" android:required="false" />` to `AndroidManifest.xml`; verify `.\gradlew.bat lint` reports no permission issues

## 3. BarcodeAnalyzer

- [x] 3.1 Create `app/src/main/java/com/example/kotlin/qrcode/BarcodeAnalyzer.kt` implementing `ImageAnalysis.Analyzer`; it must pass each `ImageProxy` frame to ML Kit's `BarcodeScanner`, close the proxy after analysis, debounce repeated results (default 2 s), and invoke `onBarcodeDetected(String)` on the main thread; verify with a unit test that the debounce suppresses a second identical value within the window
- [x] 3.2 Configure `BarcodeScanner` with `BarcodeScannerOptions` set to `ALL_FORMATS` (covers QR, EAN, Code128, etc.); verify the options are applied by inspecting the scanner instance in a unit test or via code review

## 4. CameraPreview Composable

- [x] 4.1 Create `app/src/main/java/com/example/kotlin/qrcode/CameraPreview.kt` as an internal `@Composable` that wraps `PreviewView` in `AndroidView`, binds a `Preview` use case to `ProcessCameraProvider` tied to the `LocalLifecycleOwner`, and fills the given `Modifier`; verify it renders without crash on a device/emulator

## 5. QrCodeReaderScreen Composable

- [x] 5.1 Create `app/src/main/java/com/example/kotlin/qrcode/QrCodeReaderScreen.kt` exposing `fun QrCodeReaderScreen(modifier: Modifier, onBarcodeDetected: (String) -> Unit, onPermissionDenied: () -> Unit)`; it must check/request `CAMERA` permission at composition time, show a rationale/settings UI when permanently denied, and compose `CameraPreview` + bind `ImageAnalysis` with `BarcodeAnalyzer` when permission is granted; verify the permission-denied branch shows the fallback UI
- [x] 5.2 Ensure CameraX use cases are unbound in a `DisposableEffect` `onDispose` block (or equivalent) so the camera is released when the composable leaves composition; verify by rotating the device or navigating away and confirming no `CameraException` or resource leak in Logcat

## 6. Demo Entry Point

- [x] 6.1 Create `app/src/main/java/com/example/kotlin/MainActivity.kt` with a `ComponentActivity` that sets content to `QrCodeReaderScreen` and logs/toasts the detected barcode value; add the activity to `AndroidManifest.xml` as the launcher activity; verify `.\gradlew.bat assembleDebug` succeeds and the app launches on a device/emulator showing the camera preview

## 7. Verification

- [x] 7.1 Run `.\gradlew.bat testDebugUnitTest` — all unit tests pass
- [ ] 7.2 Point the camera at a QR code on a physical device or emulator with camera support — `onBarcodeDetected` is called within ≤ 1 second with the correct decoded string, and repeated callbacks for the same code are suppressed for 2 seconds
