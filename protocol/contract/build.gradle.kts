import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

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
    val xcframeworkName = "SharedContract"
    val xcf = XCFramework(xcframeworkName)

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = xcframeworkName
            xcf.add(this)
            isStatic = true
        }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.resources)
        }
    }
}