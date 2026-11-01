plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlin.plugin.serialization)
    `maven-publish`
    alias(libs.plugins.touchlab.skie)
}

group = "com.example.protocol"
version = "0.0.1"

kotlin {
    jvmToolchain(17)
    jvm()
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}