rootProject.name = "BookManagerHandsOn"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include("protocol")
include("protocol:contract")
include("protocol:client")