import PureScriptKeys._

name := "play-purescript-example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

pscOptions in Assets := Seq("--module", "Main", "--main")
