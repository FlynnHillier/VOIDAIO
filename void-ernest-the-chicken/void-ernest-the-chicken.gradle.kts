version = "0.0.2"

project.extra["PluginName"] = "void-earnest-the-chicken"
project.extra["PluginDescription"] = "complete the earnest-the-chicken quest."

dependencies {
    implementation(project(mapOf("path" to ":void-utils")))
    compileOnly(project(":void-utils"))
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
                        ).joinToString(),
            ))
        }
    }
}