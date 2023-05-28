import org.gradle.jvm.tasks.Jar
import java.net.URI
import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
    java
    id("com.github.johnrengelman.shadow") version ("7.1.2")
    kotlin("jvm") version "1.8.0"
    id("org.jetbrains.kotlin.plugin.lombok") version "1.8.0"
    `maven-publish`
}

group = "com.xbaimiao.template"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://maven.xbaimiao.com/repository/releases/")
    maven {
        url = uri("https://maven.xbaimiao.com/repository/maven-private/")
        credentials {
            username = project.findProperty("user").toString()
            password = project.findProperty("password").toString()
        }
    }
}

dependencies {
    implementation("com.xbaimiao:EasyLib:2.0.5")
    implementation(kotlin("stdlib-jdk8"))
//    implementation ("de.tr7zw:item-nbt-api:2.11.2")
//    implementation ("com.j256.ormlite:ormlite-core:6.1")
//    implementation ("com.j256.ormlite:ormlite-jdbc:6.1")
//    implementation ("com.zaxxer:HikariCP:4.0.3")
    compileOnly(fileTree("libs"))
    compileOnly(dependencyNotation = "org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
//    compileOnly ("com.mojang:authlib:1.5.21")
}

fun releaseTime() = LocalDate.now().format(DateTimeFormatter.ofPattern("y.M.d"))

// 混淆
tasks.register("confuse") {
    this.group = "build"
    dependsOn("build")
    doLast {
        val jarName = "${project.name}-${releaseTime()}-${project.version}-all.jar"
        val jarFile = File("build/libs/$jarName")
        val allatoriCrackFile = File("allatori/allatori_crack.jar")
        if (!allatoriCrackFile.exists()) {
            throw RuntimeException("allatori_crack.jar not found")
        }
        if (!jarFile.exists()) {
            throw RuntimeException("$jarName not found")
        }
        val bakConfig = File("allatori/config-bak.xml")
        val config = File("allatori/config.xml")
        if (!config.exists()) {
            config.createNewFile()
        }
        config.writeText(
            bakConfig.readText().replace("{input}", "../" + jarFile.path)
                .replace("{out}", "../" + jarFile.path.replace(".jar", "-confuse.jar"))
                .replace("{main}", "${project.group}.${project.name}")
        )
        // 执行控制台命令
        exec {
            // 指定命令
            commandLine("java", "-jar", "allatori/allatori_crack.jar", "allatori/config.xml")
        }
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    shadowJar {
        val relocateFile = File("relocate.properties")
        relocateFile.readText().split("\n").forEach {
            val args = it.split("=")
            relocate(args[0], "${project.group}.shadow.${args[1]}")
        }
        dependencies {
            exclude(dependency("org.slf4j:"))
            exclude(dependency("com.google.code.gson:gson:"))
        }
        exclude("LICENSE")
        exclude("META-INF/*.SF")
        minimize()
        archiveBaseName.set("${project.name}-${releaseTime()}")
    }
    processResources {
        val props = ArrayList<Pair<String, Any>>()
        props.add("version" to "${releaseTime()}-$version")
        props.add("main" to "${project.group}.${project.name}")
        props.add("name" to project.name)
        expand(*props.toTypedArray())
        filteringCharset = "UTF-8"
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
    artifacts {
        archives(shadowJar)
        archives(kotlinSourcesJar)
    }
}

tasks.register("sourcesJar", Jar::class.java) {
    this.group = "build"
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    repositories {
        maven {
            credentials {
                username = project.findProperty("user").toString()
                password = project.findProperty("password").toString()
            }
            url = URI("https://maven.xbaimiao.com/repository/maven-private/")
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = "${project.version}"
            from(components["java"])
            artifact(tasks["sourcesJar"])
        }
    }
}
