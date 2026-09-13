## Why

The app needs to scan barcodes and QR codes, a foundational feature in many Android apps. ML Kit Barcode Scanning (Google) is the most widely used library for this on Android — it's free, on-device, fast, and has first-class Kotlin/Jetpack Compose support.

## What Changes

- Add ML Kit Barcode Scanning dependency (`com.google.mlkit:barcode-scanning`)
- Add CameraX dependencies for camera preview and image analysis
- Implement a reusable `QrCodeReaderScreen` composable backed by CameraX + ML Kit
- Provide a `BarcodeAnalyzer` image analysis use case that decodes QR codes and barcodes from the camera feed and emits results via a callback

## Capabilities

### New Capabilities

- `qrcode-reader`: A self-contained QR code / barcode reader component — includes camera permission handling, live CameraX preview, ML Kit-powered barcode detection, and a result callback consumed by the host screen.

### Modified Capabilities

_(none)_

## Impact

- **Dependencies**: new entries in `gradle/libs.versions.toml` for `mlkit-barcode-scanning`, `camerax-core`, `camerax-camera2`, `camerax-lifecycle`, `camerax-view`; applied in `app/build.gradle.kts`
- **Code**: new files under `app/src/main/java/com/example/kotlin/qrcode/`
- **Manifest**: `android.permission.CAMERA` permission required
- **Kotlin plugin**: must be added (per project AGENTS.md) since Kotlin sources do not compile without `org.jetbrains.kotlin.android`
