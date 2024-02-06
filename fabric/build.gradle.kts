apply(plugin = "platform")

architectury.fabric()

operator fun String.invoke(): String {
    return rootProject.ext[this] as? String
            ?: throw IllegalStateException("Property $this is not defined")
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${"fabric_version"()}")
    modApi(fabricApi.module("fabric-command-api-v2", "fabric_api_version"() + "+" + "minecraft_version"()))
    modLocalRuntime("net.fabricmc.fabric-api:fabric-api-deprecated:${"fabric_api_version"()}+${"minecraft_version"()}")

    modLocalRuntime("com.terraformersmc:modmenu:${"modmenu_version"()}")
}

tasks.shadowJar {
    filesMatching("fabric.mod.json") {
        file.readText().replace("dev.rdh.f3.F3", "dev.rdh.f3.fabric.F3").let { file.writeText(it) }
    }
}