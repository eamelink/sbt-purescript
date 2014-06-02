import bintray.Keys._

sbtPlugin := true

organization := "net.eamelink.sbt"

name := "sbt-purescript"

version := "0.3.0"

scalaVersion := "2.10.4"

scalacOptions += "-feature"

addSbtPlugin("com.typesafe.sbt" % "sbt-web" % "1.0.0")

publishMavenStyle := false

bintrayPublishSettings

repository in bintray := "sbt-plugins"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

bintrayOrganization in bintray := None

pomExtra := (
  <scm>
    <url>git@github.com:eamelink/sbt-purescript.git</url>
    <connection>scm:git:git@github.com:eamelink/sbt-purescript.git</connection>
  </scm>
  <developers>
    <developer>
      <id>eamelink</id>
      <name>Erik Bakker</name>
      <url>https://github.com/eamelink</url>
    </developer>
  </developers>
)
