plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.example.protocol"
version = "0.0.1"

kotlin {
    jvmToolchain(17)


    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.resources)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.encoding)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation("io.ktor:ktor-client-encoding:3.6.0")
            api(project(":protocol:contract"))
        }
    }
}
