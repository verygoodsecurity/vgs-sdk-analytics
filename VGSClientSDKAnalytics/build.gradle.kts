import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.mokkery)
}

kotlin {
    androidTarget {
        tasks.withType<KotlinJvmCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_1_8)
            }
        }
        publishLibraryVariants("release")
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

android {
    namespace = "com.verygoodsecurity.sdk.analytics"
    compileSdk = 35
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
        val target = file("$rootDir/VGSClientSDKAnalytics/build/XCFrameworks/release/VGSClientSDKAnalytics.xcframework")
        val destination = file("${rootDir.parent}/Frameworks/VGSClientSDKAnalytics.xcframework")

        if (!target.exists()) {
            throw GradleException("The VGSClientSDKAnalytics.framework does not exist. Ensure the assembleXCFramework task runs successfully.")
        }

        target.copyRecursively(destination, overwrite = true)
    }
}
