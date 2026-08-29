# AGENTS.md

## Project shape

- Single-module Android app (`:app`), namespace/applicationId `com.example.kotlin`.
- Fresh template: `app/src/main/` has **no source code and no Activity**; the manifest declares no launcher activity. New code goes in `app/src/main/java/com/example/kotlin/` (must be created).
- Version catalog is the single source for deps/plugins: `gradle/libs.versions.toml`.

## Commands (Windows, run from repo root)

- Build: `.\gradlew.bat assembleDebug`
- Unit tests (JVM, `app/src/test/`): `.\gradlew.bat testDebugUnitTest`
- Instrumented tests (`app/src/androidTest/`): `.\gradlew.bat connectedDebugAndroidTest` — requires a running emulator or device.
- No lint/format/typecheck tasks configured beyond `.\gradlew.bat lint`.

## Gotchas

- **No Kotlin plugin is applied.** Despite the project name, only `com.android.application` is in the catalog/build files. Kotlin sources will not compile until `org.jetbrains.kotlin.android` is added to `libs.versions.toml` and applied in both `build.gradle.kts` files.
- **AGP 9.3.2 / Gradle 9.5.0 use the new DSL.** `compileSdk { version = release(37) }` and `optimization { enable = false }` replace the old `compileSdk = 37` and `minifyEnabled false` — old-syntax snippets from docs/SO will fail.
- Daemon JVM toolchain is Java 25 (auto-provisioned via foojay-resolver); source/target compatibility is Java 11.
- `RepositoriesMode.FAIL_ON_PROJECT_REPOS`: never add `repositories {}` to module build files; add them in `settings.gradle.kts`.
- Configuration cache is enabled; avoid task-time access to `project` in any custom build logic.
- `local.properties` (SDK path) is machine-local and untracked — do not commit.
