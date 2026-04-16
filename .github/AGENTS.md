# AGENTS.md

This file defines repository-specific guidance for AI coding agents working in `vgs-sdk-analytics`.

## Scope
- This repository is a Kotlin Multiplatform analytics SDK with demo apps for Android and iOS.
- Use this file for architecture, module boundaries, and validation workflow.
- Prefer narrow, module-local changes; avoid touching demo apps unless validation or examples require it.
- When changing public API or behavior, update docs/examples in the same change.
- When changing behavior, assess whether tests should be added/updated and include them when applicable.

## 1. Core Concepts (Mental Model)
- Shipping library module: `VGSClientSDKAnalytics/`.
- Demo Android app module: `androidApp/`.
- Demo iOS app project: `iosApp/`.
- Primary runtime flow: consumer creates `VGSSharedAnalyticsManager` -> captures `VGSAnalyticsEvent` -> repository maps payload -> Ktor client sends request.

### Change Surface Map
- Public API entrypoint: `VGSClientSDKAnalytics/src/commonMain/kotlin/com/verygoodsecurity/sdk/analytics/VGSSharedAnalyticsManager.kt`.
- Event and analytics models: `VGSClientSDKAnalytics/src/commonMain/kotlin/com/verygoodsecurity/sdk/analytics/model/`.
- Data layer and mapper logic: `VGSClientSDKAnalytics/src/commonMain/kotlin/com/verygoodsecurity/sdk/analytics/data/`.
- Shared utilities (time, session, device info, serialization): `VGSClientSDKAnalytics/src/commonMain/kotlin/com/verygoodsecurity/sdk/analytics/utils/`.
- Platform-specific utilities/clients:
  - Android: `VGSClientSDKAnalytics/src/androidMain/`
  - iOS: `VGSClientSDKAnalytics/src/iosMain/`
- Demo usage examples:
  - Android: `androidApp/src/main/java/com/verygoodsecurity/sdk/analytics/android/MainActivity.kt`
  - iOS: `iosApp/iosApp/ContentView.swift`

> **Note:** `Logger.kt` exists independently in both `androidMain` and `iosMain` (not via `expect`/`actual`). When modifying logging behavior, update both files to keep them consistent.

## 2. Module Boundaries
- Modules in `settings.gradle.kts`:
  - `:VGSClientSDKAnalytics`
  - `:androidApp`
- Dependency and plugin versions are centralized in `gradle/libs.versions.toml`.
- The `:iosApp` project is an Xcode project, not a Gradle module; it consumes the SDK via the XCFramework output from `:VGSClientSDKAnalytics:assembleXCFramework`.

## 3. Environment Preconditions
- Java toolchain and JVM target are `17` for both `:VGSClientSDKAnalytics` and `:androidApp`.
- Android SDK levels are `compileSdk 36`, `targetSdk 36` (app), `minSdk 21`.
- KMP targets include `android`, `iosX64`, and `iosArm64`.
- Ensure `local.properties` contains a valid `sdk.dir` pointing to your Android SDK installation before running Android builds.

## 4. Public API and Interop Expectations
- Keep `VGSSharedAnalyticsManager` and analytics model APIs source-compatible unless a versioned breaking change is intentional.
- For JVM/Java interop-sensitive APIs (for example companion factory methods), ensure Java call patterns remain ergonomic.
- Keep event constants/types aligned with `Constants.kt` and event model mapping logic.
- When adding new `expect`/`actual` declarations, provide implementations in **both** `androidMain` and `iosMain`.

## 5. Tests and Validation
- Unit tests live in `VGSClientSDKAnalytics/src/commonTest/`.
- Test files currently cover: `AnalyticsManagerTest`, `EventTest`, `MappingPolicyTest`, `StatusTest`, `UpstreamTest`.
- For library behavior/API changes, run at minimum:
  - `./gradlew :VGSClientSDKAnalytics:testDebugUnitTest --continue`
- For broader verification (or dependency/build logic changes), also run:
  - `./gradlew :VGSClientSDKAnalytics:assemble --continue`
  - `./gradlew :androidApp:assembleDebug --continue`
- When adding new public behavior, add a corresponding test in `commonTest` before finishing.
- Do **not** add platform-specific test sources unless the logic is genuinely platform-dependent and cannot be tested in `commonTest`.
- iOS-specific tests should be added in the Xcode project as needed, ensuring they target the correct architecture (e.g., iOS Simulator vs. physical device).

## 6. iOS Framework Output
- `:VGSClientSDKAnalytics:assembleXCFramework` produces `VGSClientSDKAnalytics.xcframework`.
- Build script copies the framework to `../Frameworks/VGSClientSDKAnalytics.xcframework`; avoid breaking this path unless intentionally updating iOS integration workflow.
- After rebuilding the XCFramework, open `iosApp/iosApp.xcworkspace` to verify the demo app still builds against the new framework.

## 7. Logging and Privacy
- Do not log sensitive user payloads or raw event values that may contain customer data.
- Keep debug logging minimal and avoid adding permanent verbose logs in shared code paths.
- Platform loggers (`Logger.kt` in `androidMain` and `iosMain`) should only be used for debug-level diagnostics and must be silent in release builds.

## 8. Dependency Management
- All library versions must be declared in `gradle/libs.versions.toml`; do not hardcode version strings in module `build.gradle.kts` files.
- After updating a dependency version, run `./gradlew :VGSClientSDKAnalytics:assemble --continue` to verify no API breakage was introduced.

## 9. Final Rule for Agents
- Prefer the smallest safe change that satisfies the task.
- Validate behavior with relevant tests before finishing.
- If API shape changes, update examples and docs in the same PR.
- Never commit credentials, tokens, or internal endpoint URLs; use constants or configuration injection patterns instead.
