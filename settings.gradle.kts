pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.neoforged.net/")
        gradlePluginPortal()
    }
}

rootProject.name = "F3 Command"

include("common")
include("fabric")
include("forge")
include("neoforge")
project(":neoforge").name = "neoForge"