apply(plugin = "platform")

architectury.forge()

operator fun String.invoke(): String {
    return rootProject.ext[this] as? String
            ?: throw IllegalStateException("Property $this is not defined")
}

dependencies {
    forge("net.minecraftforge:forge:${"minecraft_version"()}-${"forge_version"()}")
}