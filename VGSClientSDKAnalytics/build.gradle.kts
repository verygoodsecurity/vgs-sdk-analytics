import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.mokkery)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
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

    js(IR) {
        moduleName = "VGSClientSDKAnalytics"
        browser {
            webpackTask {
                mainOutputFileName = "VGSClientSDKAnalytics.js"
                output.libraryTarget = "commonjs2"
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.kotlin.coroutines.core)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        jsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.coroutines.test)
        }
    }
}

android {
    namespace = "com.verygoodsecurity.sdk.analytics"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        buildConfig = true
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

tasks.register("assembleJsBundle") {
    dependsOn("jsBrowserProductionWebpack")
    doLast {
        val target =
            file("$rootDir/VGSClientSDKAnalytics/build/kotlin-webpack/js/productionExecutable/VGSClientSDKAnalytics.js")

        val destination = file("${rootDir.parent}/analytics/VGSClientSDKAnalytics.js")

        println(target)
        println(destination)

        if (!target.exists()) {
            throw GradleException("The VGSClientSDKAnalytics.js does not exist. Ensure the assembleJsBundle task runs successfully.")
        }

        target.copyRecursively(destination, overwrite = true)
    }
}
