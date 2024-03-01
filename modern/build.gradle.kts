// for 1.20.2+
import xyz.wagyourtail.unimined.api.minecraft.MinecraftConfig
import xyz.wagyourtail.unimined.api.unimined

val minecraftVersion = "1.20.4"
val forgeVersion = "49.0.27"
val neoVersion = "153-beta"
val parchmentVersion = "2023.12.31"
val parchmentMcVersion = "1.20.3"
val fabricApiVersion = "0.93.1"

sourceSets {
    maybeCreate("forge").apply {
        unimined.minecraft(this) {
            setup()
            minecraftForge {
                loader(forgeVersion)
            }
        }
    }
    maybeCreate("neoforge").apply {
        unimined.minecraft(this) {
            setup()
            neoForged {
                loader(neoVersion)
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
//            fabricModImplementation("net.fabricmc.fabric-api:fabric-api:0.91.3+$minecraftVersion")
            fabricModImplementation(unimined.fabricApi.fabricModule("fabric-command-api-v2", "$fabricApiVersion+$minecraftVersion"))
        }
    }
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.WARN

    inputs.property("version", project.version)

    filesMatching("META-INF/mods.toml") {
        expand("version" to project.version.toString())
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