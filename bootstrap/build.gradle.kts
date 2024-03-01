repositories {
    maven("https://maven.fabricmc.net")
}

dependencies {
    compileOnly("net.fabricmc:fabric-loader:${rootProject.ext["fabric_version"]}")
    compileOnly(rootProject)
}

val thisProject = project
rootProject.subprojects.forEach {
    if (it != thisProject) {
        sourceSets.forEach a@ { s ->
            if(s.name == "main" || s.name == "test") return@a
            thisProject.dependencies {
                compileOnly(files(s.java.srcDirs))
            }
        }
    }
}

tasks.processResources {
    outputs.upToDateWhen{false}
    inputs.property("version", rootProject.version)
    filesMatching(listOf("META-INF/mods.toml", "fabric.mod.json")) {
        expand("version" to rootProject.version)
    }
}
