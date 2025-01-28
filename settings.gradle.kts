enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "vgs-sdk-analytics"
include(":androidApp")
include(":VGSClientSDKAnalytics") // We use this module name patter to achieve correct framework name for iOS