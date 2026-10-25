plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlin.plugin.serialization)
    `maven-publish`
}

group = "com.example.protocol"
version = "0.0.1"

kotlin {
    jvmToolchain(17)
    jvm()
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.resources)
        }
    }
}