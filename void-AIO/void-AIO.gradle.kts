version = "0.0.2"

project.extra["PluginName"] = "void-AIO"
project.extra["PluginDescription"] = "does the fokkin lot."

dependencies {
    compileOnly(project(":void-utils"))
    compileOnly(project(":void-woodcutting"))
    compileOnly("io.socket:socket.io-client:2.1.0")
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
}