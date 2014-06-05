package net.eamelink.sbt.purescript

import com.typesafe.sbt.web._
import sbt._
import sbt.Keys._
import sbt.ProcessLogger
import scala.collection.mutable.ArrayBuffer
import scala.util.{ Failure, Success, Try }
import xsbti.{ Problem, Severity }

object Import {
  object PureScriptKeys {
    val purescript = TaskKey[Seq[File]]("purescript", "Invoke the PureScript compiler.")
    val psci = TaskKey[Unit]("psci", "Start a Purescript REPL with sources loaded.")

    val executable = SettingKey[String]("purescript-executable", "The PureScript executable.")
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
    sources in purescript := (sourceDirectories.value ** ((includeFilter in purescript).value -- (excludeFilter in purescript).value)).get,

    purescript := {
      streams.value.log.info(s"Purescript compiling on ${(sources in purescript).value.length} source(s)")

      val command = executable.value :: pscOptions.value.toList ++ ("--output" :: output.value.absolutePath :: (sources in purescript).value.getPaths.toList)

      val problems = doCompile(command, sources.value)
      CompileProblems.report((reporter in purescript).value, problems)

      Seq(output.value)
    },

    (executable in psci) := "psci",

    psci := {
      streams.value.log.info(s"Starting ${executable.value} with ${(sources in purescript).value.length} source files...")
      val command = (executable in psci).value :: (sources in purescript).value.getPaths.toList
      Process(command).run(true).exitValue()
    },

    resourceGenerators <+= purescript)

  override def projectSettings =
    inConfig(Assets)(basePureScriptSettings ++ Seq(
      resourceManaged in purescript := webTarget.value / "purescript" / "main")) ++
      inConfig(TestAssets)(basePureScriptSettings ++ Seq(
        resourceManaged in purescript := webTarget.value / "purescript" / "test")) ++ Seq(
        purescript := (purescript in Assets).value,
        psci := (psci in Assets).value)

  def doCompile(command: Seq[String], sourceFiles: Seq[File]): Seq[Problem] = {
    val (buffer, pscLogger) = logger
    val exitStatus = command ! pscLogger
    if (exitStatus != 0) PscOutputParser.readProblems(buffer mkString "\n", sourceFiles).get
    else Nil
  }

  def logger = {
    val lineBuffer = new ArrayBuffer[String]
    val logger = new ProcessLogger {
      override def info(s: => String) = lineBuffer += s
      override def error(s: => String) = lineBuffer += s
      override def buffer[T](f: => T): T = f
    }
    (lineBuffer, logger)
  }

  object PscOutputParser {
    val TypeError = """(?s)Error at (.*) line ([0-9]+), column ([0-9]+):\s*\n(.*)""".r
    val ParseError = """(?s)"([^"]+)" \(line ([0-9]+), column ([0-9]+)\):\s*\n(.*)""".r

    def readProblems(pscOutput: String, sourceFiles: Seq[File]): Try[Seq[Problem]] = pscOutput match {
      case TypeError(filePath, lineString, columnString, message) =>
        Success(Seq(problem(filePath, lineString, columnString, message)))
      case ParseError(filePath, lineString, columnString, message) =>
        Success(Seq(problem(filePath, lineString, columnString, message)))
      case other =>
        Failure(new RuntimeException(s"Failed to parse `psc` output. This is the original `psc` output:\n" + pscOutput))
    }

    def problem(filePath: String, lineString: String, columnString: String, message: String) = {
      val file = new File(filePath)
      val line = lineString.toInt
      val column = columnString.toInt - 1
      new LineBasedProblem(message, Severity.Error, line, column, IO.readLines(file).drop(line - 1).headOption.getOrElse(""), file)
    }
  }
}