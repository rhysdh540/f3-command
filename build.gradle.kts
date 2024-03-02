@file:Suppress("NestedLambdaShadowedImplicitParameter")

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import xyz.wagyourtail.unimined.api.task.RemapJarTask
import xyz.wagyourtail.unimined.api.unimined
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.Deflater

plugins {
    java
    id("xyz.wagyourtail.unimined") version("1.1.2-SNAPSHOT") apply(false)
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

setup()

allprojects {
    apply(plugin = "java")

    base.archivesName = "archives_base_name"()
    version = "modVersion"()
    group = "maven_group"()

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release = 17
    }

    repositories.mavenCentral()
}

repositories {
    maven("https://libraries.minecraft.net")
}

subprojects {
    if(!this.isMinecraftSubproject()) return@subprojects
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "xyz.wagyourtail.unimined")

    repositories {
        maven("https://maven.parchmentmc.org")
        maven("https://mcentral.firstdark.dev/releases")
    }

    val rootOutput = rootProject.sourceSets["main"].output

    sourceSets {
        project.file("src").list()?.forEach {
            maybeCreate(it).apply {
                java.srcDir("src/$it/java")
                resources.srcDir("src/$it/resources")
                compileClasspath += rootOutput
                runtimeClasspath += rootOutput

                unimined.minecraft(this, true) {
                    combineWith(rootProject.sourceSets["main"])
                    runs.config("server") {
                        disabled = true
                    }
                }
            }
        }
    }

    tasks.shadowJar {
        archiveClassifier = project.name

        val remapTasks = tasks.withType<RemapJarTask>().matching {
            it.group == "unimined" && it.name.matches(Regex("remap.*Jar"))
        }
        remapTasks.forEach {
            from(it)
            dependsOn(it)
        }
    }
}

dependencies {
    compileOnly("com.mojang:brigadier:1.0.18")
    compileOnly("org.slf4j:slf4j-api:2.0.9")
}

tasks.shadowJar {
    subprojects.forEach {
        if(!it.isMinecraftSubproject()) return@forEach

        it.tasks.shadowJar.get().also {
            println("Adding $it to $this")
            from(zipTree(it.archiveFile.get())) {
                include { it.isDirectory || it.name.endsWith(".class") }
            }
            dependsOn(it)
        }
    }

    project(":bootstrap").tasks.jar.get().also {
        from(it)
        dependsOn(it)
    }

    archiveBaseName = "archives_base_name"()
    archiveClassifier = ""
    archiveVersion = "modVersion"()

    doLast {
        val jar = archiveFile.get().asFile
        val contents = linkedMapOf<String, ByteArray>()
        JarFile(jar).use {
            it.entries().asIterator().forEach { entry ->
                if(!entry.isDirectory) {
                    contents[entry.name] = it.getInputStream(entry).readAllBytes()
                }
            }
        }

        jar.delete()

        JarOutputStream(jar.outputStream()).use { out ->
            out.setLevel(Deflater.BEST_COMPRESSION)
            contents.forEach { var (name, bytes) = it
                if(name.endsWith(".json") || name.endsWith(".mcmeta")) {
                    bytes = JsonOutput.toJson(JsonSlurper().parse(bytes)).toByteArray()
                }

                if(name.endsWith(".class")) {
                    val node = ClassNode()
                    ClassReader(bytes).accept(node, 0)

                    node.methods.forEach { method ->
                        method.localVariables?.clear()
                        method.parameters?.clear()
                    }

                    // Remove @Mod annotation from forge impls except for the bootstrap
                    if(!name.equals("dev/rdh/f3/bootstrap/F3ForgeBootstrap.class", false)) {
                        node.visibleAnnotations?.removeIf { it.desc == "Lnet/minecraftforge/fml/common/Mod;" }
                    }

                    val writer = ClassWriter(0)
                    node.accept(writer)
                    bytes = writer.toByteArray()
                }

                out.putNextEntry(JarEntry(name))
                out.write(bytes)
                out.closeEntry()
            }
            out.finish()
            out.close()
        }
    }
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

fun setup() {
    println("${project.name} v${"mod_version"()}")
    val buildNumber = System.getenv("GITHUB_RUN_NUMBER")
    if(buildNumber != null) {
        println("Build #$buildNumber")
        ext["build_number"] = buildNumber
    } else ext["build_number"] = null
    println()

    apply(plugin = "xyz.wagyourtail.unimined")

    ext["modVersion"] = "mod_version"() + (if(buildNumber != null) "-build.$buildNumber" else "")

    tasks.register("nukeGradleCaches") {
        dependsOn("clean")
        group = "build"
        doLast {
            allprojects.forEach {
                it.file(".gradle").deleteRecursively()
            }
        }
    }
}

operator fun String.invoke(): String {
    return rootProject.ext[this] as? String
            ?: throw IllegalStateException("Property $this is not defined")
}


fun Project.isMinecraftSubproject(): Boolean {
    return (this in arrayOf(
            rootProject, project(":bootstrap"), project(":stub")
    )).not()
}