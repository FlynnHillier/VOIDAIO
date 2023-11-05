version = "0.0.2"

project.extra["PluginName"] = "void-AIO"
project.extra["PluginDescription"] = "does the fokkin lot."

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

dependencies {
    implementation(project(":void-utils"))
    implementation(project(":void-woodcutting"))
    implementation("io.socket:socket.io-client:2.1.0")
}

tasks {
    jar {
        manifest {
            attributes(mapOf(
                "Plugin-Version" to project.version,
                "Plugin-Id" to nameToId(project.extra["PluginName"] as String),
                "Plugin-Provider" to project.extra["PluginProvider"],
                "Plugin-Description" to project.extra["PluginDescription"],
                "Plugin-License" to project.extra["PluginLicense"],
                "Plugin-Dependencies" to
                        arrayOf(
                            nameToId("void-utils"),
                            nameToId("void-woodcutting")
                        ).joinToString(),
            ))
        }
    }
    shadowJar {
        archiveClassifier.set("") // Set to empty to replace the default jar
        manifest {
            attributes(
                "Plugin-Version" to project.version,
                "Plugin-Id" to nameToId(project.extra["PluginName"] as String),
                "Plugin-Provider" to project.extra["PluginProvider"],
                "Plugin-Description" to project.extra["PluginDescription"],
                "Plugin-License" to project.extra["PluginLicense"],
                "Plugin-Dependencies" to arrayOf(
                    nameToId("void-utils"),
                    nameToId("void-woodcutting")
                ).joinToString(", ")
            )
        }
        configurations = listOf(project.configurations.runtimeClasspath.get())

        // Exclude the module-info.class file to prevent NoClassDefFoundError
        exclude("META-INF/versions/9/module-info.class")
        exclude("module-info.class") // You might need this line as well if you have module-info.class outside of META-INF/versions/9/
    }
}

