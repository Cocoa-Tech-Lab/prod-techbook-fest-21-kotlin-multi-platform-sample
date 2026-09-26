plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    `maven-publish`
}

group = "com.example.protocol"
version = "0.0.1"

kotlin {
    jvmToolchain(17)

    android {
        namespace = "com.example.bookmanager.protocol.client"
        compileSdk = 37
    }
    jvm()
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.resources)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.encoding)
            implementation(libs.ktor.serialization.kotlinx.json)
            api(project(":protocol:contract"))
        }

        jvmMain.dependencies {
            // Source: https://mvnrepository.com/artifact/io.ktor/ktor-client-okhttp
            implementation("io.ktor:ktor-client-okhttp:3.6.0")
        }

        androidMain.dependencies {
            // Source: https://mvnrepository.com/artifact/io.ktor/ktor-client-okhttp
            implementation("io.ktor:ktor-client-okhttp:3.6.0")
        }
    }
}