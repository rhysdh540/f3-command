pluginManagement {
    repositories {
        maven("https://mcentral.firstdark.dev/releases")
        maven("https://maven.firstdarkdev.xyz/snapshots")
        gradlePluginPortal()
    }
}

rootProject.name = "F3 Command"

include("modern", "lessModern")
include("bootstrap")
