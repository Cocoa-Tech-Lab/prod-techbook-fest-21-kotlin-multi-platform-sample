plugins {
    kotlin("jvm")
}

group = "com.example"
version = "0.0.1"

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}