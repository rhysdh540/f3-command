// for 1.19.0-1.20.1
import xyz.wagyourtail.unimined.api.minecraft.MinecraftConfig
import xyz.wagyourtail.unimined.api.unimined

val minecraftVersion = "1.20.1"
val forgeVersion = "47.2.19"
val parchmentVersion = "2023.09.03"
val parchmentMcVersion = "1.20.1"
val fabricApiVersion = "0.92.0"

sourceSets {
    maybeCreate("forge").apply {
        unimined.minecraft(this) {
            setup()
            minecraftForge {
                loader(forgeVersion)
            }
        }
    }
    maybeCreate("fabric").apply {
        unimined.minecraft(this) {
            setup(intermediary = true)
            fabric {
                loader("fabric_version"())
            }
        }
        dependencies {
            fabricModImplementation(unimined.fabricApi.fabricModule("fabric-command-api-v2", "$fabricApiVersion+$minecraftVersion"))
        }
    }
}

tasks.withType<ProcessResources> {
    outputs.upToDateWhen { false }
    duplicatesStrategy = DuplicatesStrategy.WARN
    inputs.property("version", project.version)

    filesMatching(listOf("**/fabric.mod.json", "META-INF/mods.toml")) {
        expand("version" to project.version)
    }
}

fun MinecraftConfig.setup(intermediary: Boolean = false) {
    version = minecraftVersion
//    side("client")
    mappings {
        if(intermediary) {
            intermediary()
        } else {
            searge()
        }
        mojmap()
        parchment(mcVersion = parchmentMcVersion, version = parchmentVersion)

        devNamespace("mojmap")
    }

    runs.config("client") {
        disabled = false
    }
}

operator fun String.invoke(): String = rootProject.ext[this] as? String ?: throw IllegalStateException("Property $this is not defined")