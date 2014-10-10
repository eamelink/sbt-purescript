import PureScriptKeys._

lazy val root = (project in file(".")).enablePlugins(SbtWeb)

val purescriptCompiled = taskKey[Unit]("Test if the last command was purescript compiling something")

