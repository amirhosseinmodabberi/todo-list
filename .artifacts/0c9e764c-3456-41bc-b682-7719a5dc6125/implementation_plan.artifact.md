# Fix Gradle Sync Error by Correcting Dependency Versions

The project is currently failing to sync because it references non-existent or invalid versions of the Android Gradle Plugin (AGP) and other dependencies in `gradle/libs.versions.toml` and root `build.gradle.kts`.

## User Review Required

> [!IMPORTANT]
> I am proposing to update the versions of several core dependencies (AGP, Kotlin, KSP, Hilt, etc.) to their latest stable versions found in the repositories. This is necessary because the current versions (e.g., AGP 8.12.3) cannot be resolved.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/Asus/AndroidStudioProjects/Taskcloud/gradle/libs.versions.toml)
- Update `agp` to `9.3.0`
- Update `kotlin` to `2.4.10`
- Update `coreKtx` to `1.19.0`
- Update `lifecycleRuntimeKtx` to `2.11.0`
- Update `composeBom` to `2026.06.01`
- Update `navigationCompose` to `2.9.8`

#### [MODIFY] [build.gradle.kts (root)](file:///C:/Users/Asus/AndroidStudioProjects/Taskcloud/build.gradle.kts)
- Update Kotlin Serialization plugin version to `2.4.10`
- Update KSP plugin version to match Kotlin (`2.4.10-1.0.x` - I will verify the exact KSP version)
- Update Hilt plugin version to `2.60.1`

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/Asus/AndroidStudioProjects/Taskcloud/app/build.gradle.kts)
- Update hardcoded versions for Navigation and Serialization to match the catalog or latest stable.
- Verify `compileSdk` and `targetSdk`. If `36` is not available yet, I'll revert to `35`.

## Verification Plan

### Automated Tests
- Run `gradle_sync` to ensure the project configuration is successful.
- Run `gradle_build` to ensure the project compiles with the new versions.
