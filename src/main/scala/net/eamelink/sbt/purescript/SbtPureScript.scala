package net.eamelink.sbt.purescript

import com.typesafe.sbt.web.SbtWeb
import sbt._
import sbt.Keys._

object Import {
  object PureScriptKeys {
    val purescript = TaskKey[Seq[File]]("purescript", "Invoke the PureScript compiler.")

    val executable = SettingKey[String]("purescript-executable", "The PureScript psc executable.")
    val output = SettingKey[File]("purescript-output-file", "PureScript output js file.")
    val pscOptions = SettingKey[Seq[String]]("purescript-psc-options", "Purescript compiler options")
  }
}

object SbtPureScript extends AutoPlugin {

  override def requires = SbtWeb

  override def trigger = AllRequirements

  val autoImport = Import

  import SbtWeb.autoImport._
  import WebKeys._
  import autoImport.PureScriptKeys._

  val basePureScriptSettings = Seq(
    executable := "psc",
    pscOptions := Nil,
    output := (resourceManaged in purescript).value / "js" / "main.js",

    includeFilter in purescript := "*.purs",

    purescript := {
      val sourceFiles = (sourceDirectory.value ** ((includeFilter in purescript).value -- (excludeFilter in purescript).value)).get
      val sourcePaths = sourceFiles.getPaths.toList
      streams.value.log.info(s"Purescript compiling on ${sourcePaths.length} source(s)")

      val command = executable.value :: pscOptions.value.toList ++ ("--output" :: output.value.absolutePath :: sourcePaths)
      command ! (streams.value.log)

      Seq(output.value)
    },

    resourceGenerators <+= purescript)

  override def projectSettings =
    inConfig(Assets)(basePureScriptSettings ++ Seq(
      resourceManaged in purescript := webTarget.value / "purescript" / "main")) ++
      inConfig(TestAssets)(basePureScriptSettings ++ Seq(
        resourceManaged in purescript := webTarget.value / "purescript" / "test")) ++ Seq(
        purescript := (purescript in Assets).value)

}