import org.gradle.internal.impldep.com.amazonaws.PredefinedClientConfigurations.defaultConfig
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.mokkery)
}

kotlin {
    android {
        namespace = "com.verygoodsecurity.sdk.analytics"
        minSdk {  version = release(21) }
        compileSdk { version = release(36) }
    }

    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "VGSClientSDKAnalytics"
            xcf.add(this)
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.coroutines.test)
        }
    }
}

tasks.named("embedAndSignAppleFrameworkForXcode") {
    dependsOn("assembleXCFramework")
}

tasks.named("assembleXCFramework") {
    doLast {
        val target =
            file("$rootDir/VGSClientSDKAnalytics/build/XCFrameworks/release/VGSClientSDKAnalytics.xcframework")
        val destination = file("${rootDir.parent}/Frameworks/VGSClientSDKAnalytics.xcframework")

        if (!target.exists()) {
            throw GradleException("The VGSClientSDKAnalytics.framework does not exist. Ensure the assembleXCFramework task runs successfully.")
        }

        target.copyRecursively(destination, overwrite = true)
    }
}
