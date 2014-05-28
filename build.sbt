sbtPlugin := true

organization := "net.eamelink.sbt"

name := "sbt-purescript"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.4"

scalacOptions += "-feature"

addSbtPlugin("com.typesafe.sbt" % "sbt-web" % "1.0.0")

publishMavenStyle := false


