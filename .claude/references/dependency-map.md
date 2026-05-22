# vgs-sdk-analytics-private Dependency Map

## Project Structure

Kotlin Multiplatform (KMP) analytics SDK published to Maven Central under
`com.verygoodsecurity`. The project targets Android and iOS (via XCFramework)
and consists of two modules:

| Module | Role |
|--------|------|
| `VGSClientSDKAnalytics` | Core KMP library — analytics client with Ktor HTTP layer. Published artifact. |
| `androidApp` | Android demo/sample app using Jetpack Compose. Not published. |
| `iosApp` | iOS demo app (Xcode project consuming the XCFramework). Not published. |

Dependencies are managed via a Gradle version catalog (`gradle/libs.versions.toml`).

## Dependency Categories

### Always Low Risk (Auto-merge Candidates)

| Pattern | Example | Reason |
|---------|---------|--------|
| Gradle Actions CI pins | `gradle/actions` | CI-only, no runtime impact |
| Mokkery (test mocking) | `dev.mokkery:mokkery` | Test-scope plugin only |
| kotlinx-coroutines-test | `org.jetbrains.kotlinx:kotlinx-coroutines-test` | Test-scope only |
| kotlin-test | `org.jetbrains.kotlin:kotlin-test` | Test-scope only |
| Compose UI tooling (debug) | `androidx.compose.ui:ui-tooling` | debugImplementation in sample app only |
| Maven Publish plugin | `com.vanniktech.maven.publish` | Build/publish tooling, no runtime impact |

### Needs Quick Review

| Pattern | Example | Reason | Expected Test Coverage |
|---------|---------|--------|----------------------|
| Compose UI libraries | `androidx.compose.ui:ui`, `material3` | Sample app only, not in published SDK | Manual sample app verification |
| AndroidX Activity Compose | `androidx.activity:activity-compose` | Sample app only | Manual sample app verification |
| Android Gradle Plugin (AGP) | `com.android.application`, `com.android.library` | Build tooling, minor bumps usually safe | CI build succeeds |
| Compose Compiler plugin | `kotlin.plugin.compose` | Tied to Kotlin version, sample app only | CI build succeeds |

### Needs Deep Review

| Pattern | Example | Reason | Expected Test Coverage |
|---------|---------|--------|----------------------|
| Kotlin monorepo | `org.jetbrains.kotlin:*` (compiler, stdlib, serialization) | Core language version — affects all targets, binary compatibility | CI build + unit tests across all targets |
| Ktor client | `io.ktor:ktor-client-core`, `ktor-client-okhttp`, `ktor-client-darwin` | Runtime HTTP layer of the published SDK — breaking changes affect consumers | Unit tests with Mokkery mocks; integration coverage depends on CI |
| Kotlin Serialization plugin | `kotlin.plugin.serialization` | Tied to Kotlin version, affects data serialization in SDK | Unit tests |

## Historical Patterns (from PR analysis)

5 open Renovate PRs as of 2026-04-14:

- **gradle/actions v6** — CI action major bump
- **Ktor monorepo v3.4.2** — runtime dependency, needs deep review
- **Kotlin monorepo v2.3.20** — language version bump, needs deep review
- **Mokkery v3.3.0** — test-only, low risk
- **Pin dependencies** — initial pinning PR, low risk

## Renovate Configuration

Minimal configuration — `renovate.json` contains only the schema reference with
no `extends`, labels, grouping, automerge rules, or package rules. This means
Renovate uses its default preset (`config:base`), which includes:

- Separate PRs per dependency (no grouping beyond default monorepo grouping)
- No automerge enabled
- Default branch targeting
- No custom labels (the `renovate` label seen on PRs comes from org-level config)
