repositories {
    maven("https://maven.fabricmc.net")
}

dependencies {
    // there's too many classes involved with getting the minecraft version on fabric to just include it in stub
    // so just depend on the whole thing
    compileOnly("net.fabricmc:fabric-loader:${rootProject.ext["fabric_version"]}")
    compileOnly("org.slf4j:slf4j-api:2.0.9")
    compileOnly(project(":stub"))
    compileOnly(rootProject)
}

tasks.processResources {
    outputs.upToDateWhen{false}
    inputs.property("version", rootProject.version)
    filesMatching(listOf("META-INF/mods.toml", "fabric.mod.json")) {
        expand("version" to rootProject.version)
    }
}
