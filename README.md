# sbt-purescript

sbt-purescript is an SBT plugin that compiles [PureScript](http://purescript.org) files to Javascript. It uses and follows the conventions of [sbt-web](https://github.com/sbt/sbt-web).

# Usage

To use a stable release, add the following to the `project/plugins.sbt` of your project:

    addSbtPlugin("net.eamelink.sbt" % "sbt-purescript" % "0.4.0")

To use the latest from Github, add the following to the `project/plugins.sbt` of your project:

    lazy val root = project.in(file(".")).dependsOn(sbtPurescript)
    lazy val sbtPurescript = uri("git://github.com/eamelink/sbt-purescript")

Then:

  * Put your PureScript files (with extension `purs` into `src/main/assets`)
  * Run the `purescript` task
  * Observe the glorious result in `target/web/public/main/js`

Or in a Play Framework project:

  * Put your PureScript files (with extension `purs` into `app/assets`)
  * Run Play
  * Observe the JS file on `http://localhost:9000/main.js`

There is also a `psci` command that will run the purescript interpreter, with your sources loaded.

## Using bower

Many purescript packages are distributed with bower. If you want to use bower to manage dependencies, you can just work with a regular `bower.json` file. To let the `purescript` task find the dependencies, add the following to `build.sbt`:

    // Include only the `src` directories from the bower packages
    (sourceDirectories in purescript in Assets) ++= (baseDirectory.value / "bower_components" * AllPassFilter / "src").get

## Options

There's a `pscOptions` key, with a sequence of parameters given to the `psc` command. You need to scope with within the `Assets` config. For example:

    import PureScriptKeys._
    pscOptions in Assets := Seq("--module", "Main", "--main") 

 

## Hacking on the plugin

Run `scripted` to run the plugin test suite (which is barely existent at this point...)

## License

MIT License, see `LICENSE` file.