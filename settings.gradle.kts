rootProject.name = "VOIDAIO"

include("void-utils")
include("void-woodcutting")
include("void-AIO")
include("void-command")
include("void-cowkill")

for (project in rootProject.children) {
    project.apply {
        projectDir = file(name)
        buildFileName = "$name.gradle.kts"

        require(projectDir.isDirectory) { "Project '${project.path} must have a $projectDir directory" }
        require(buildFile.isFile) { "Project '${project.path} must have a $buildFile build script" }
    }
}
