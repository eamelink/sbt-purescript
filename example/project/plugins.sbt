lazy val root = project.in(file(".")).dependsOn(pureScriptPlugin)

lazy val pureScriptPlugin = file("../..")
