package net.eamelink.sbt.purescript

import sbt._
import com.typesafe.sbt.web.SbtWeb
import sbt.Keys._

object Import {
  object PureScriptKeys {
    val purescript = TaskKey[Seq[File]]("purescript", "Invoke the PureScript compiler.")
    val binary = SettingKey[String]("purescript-binary", "The purescript binary.")
    val outputFile = SettingKey[File]("purescript-output-file", "Purescript output file.")
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
    includeFilter := "*.purs",
    outputFile := (public in Assets).value / "js" / "main.js",
    binary := "psc")

  override def projectSettings =
    inConfig(Assets)(basePureScriptSettings) ++
      Seq(
        purescript := {
          val sourceDir = (sourceDirectory in Assets).value
          val sources = sourceDir ** (includeFilter in Assets in purescript).value
          val sourcePaths = sources.getPaths
          streams.value.log.info(s"Purescript compiling ${sourcePaths.length} sources")

          val command = List(
            (binary in Assets).value,
            "--output", (outputFile in Assets).value.absolutePath) ++
            sourcePaths

          command ! (streams.value.log)

          Seq((outputFile in Assets).value)
        })

}