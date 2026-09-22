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
            api(project(":protocol:contract"))
        }
    }
}