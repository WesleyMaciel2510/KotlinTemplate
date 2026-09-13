## Context

This is a greenfield Android app with no existing source files. The Kotlin plugin is not yet applied (see AGENTS.md). CameraX is the Android-recommended camera API; ML Kit Barcode Scanning is Google's on-device, no-network barcode library — both must be added from scratch.

## Goals / Non-Goals

**Goals:**
- Wire ML Kit Barcode Scanning + CameraX into a single reusable Compose component
- Enable the Kotlin plugin so Kotlin sources compile
- Add all necessary Gradle dependencies via the version catalog
- Handle camera permission lifecycle correctly within the composable

**Non-Goals:**
- Torch/flashlight control
- Multiple simultaneous barcode tracking or bounding-box overlay
- Custom UI theming beyond a minimal viewfinder frame
- Barcode generation (encoding)
- Network upload of scanned values

## Decisions

### D1 — ML Kit over ZXing / ZBar
ML Kit Barcode Scanning is chosen over ZXing ("Zebra Crossing") because:
- On-device, no latency from network calls
- Google-maintained, Kotlin-first API
- Supports all major formats with one dependency
- Integrates directly with CameraX `ImageAnalysis` use case via `InputImage.fromMediaImage`

ZXing is older, requires more manual threading, and lacks first-class CameraX support.

### D2 — CameraX `ImageAnalysis` for detection, `PreviewView` for display
CameraX's `Preview` use case binds to `PreviewView` for the live feed; `ImageAnalysis` feeds frames to `BarcodeScanner`. Both are bound together on the same `ProcessCameraProvider` so they share the same camera session, reducing overhead.

### D3 — Executor-based analysis on background thread
`ImageAnalysis.Builder.setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)` is used so slow frames are dropped instead of queued, keeping latency low. Analysis runs on a dedicated `Executor` (not the main thread).

### D4 — Compose + `AndroidView` for PreviewView
`PreviewView` is a legacy View; it is wrapped in `AndroidView` inside the composable. `rememberCoroutineScope` and `LaunchedEffect` manage binding and cleanup tied to Compose lifecycle.

### D5 — Kotlin plugin addition
`org.jetbrains.kotlin.android` is added to `libs.versions.toml` (alias `kotlin-android`) and applied in both root and app `build.gradle.kts` per AGENTS.md.

### D6 — Package layout
All new files live in `app/src/main/java/com/example/kotlin/qrcode/`:
- `BarcodeAnalyzer.kt` — `ImageAnalysis.Analyzer` implementation
- `QrCodeReaderScreen.kt` — top-level Compose entry point (permission + camera)
- `CameraPreview.kt` — internal `AndroidView` wrapping `PreviewView`

## Risks / Trade-offs

- **Kotlin plugin version must match AGP** → Use the same version string as the kotlin stdlib/reflect already referenced in any transitive dep; default to the Kotlin version bundled with the current Kotlin Gradle Plugin compatible with AGP 9.3.2.
- **AGP 9.3.2 new DSL** → `compileSdk { version = release(37) }` syntax; do not use old `compileSdk = 37` style.
- **`RepositoriesMode.FAIL_ON_PROJECT_REPOS`** → ML Kit and CameraX are on `google()` and `mavenCentral()`, both already declared in `settings.gradle.kts`; no module-level repo block needed.
- **Camera permission on Android 13+** → `CAMERA` is a runtime permission; the composable must call `rememberPermissionState` (Accompanist) or the equivalent Compose API.

## Migration Plan

1. Add Kotlin plugin + CameraX + ML Kit to version catalog and build files
2. Add `CAMERA` permission to `AndroidManifest.xml`
3. Create `BarcodeAnalyzer`, `CameraPreview`, `QrCodeReaderScreen`
4. Wire `QrCodeReaderScreen` into `MainActivity` (or a nav destination) to demo the component

No data migration or rollback steps required — this is entirely additive.
