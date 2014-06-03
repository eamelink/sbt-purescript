# sbt-purescript

sbt-purescript is an SBT plugin that compiles [PureScript](http://purescript.org) files to Javascript. It uses and follows the conventions of [sbt-web](https://github.com/sbt/sbt-web).

# Usage

Add the following to the `project/plugins.sbt` of your project:

    addSbtPlugin("net.eamelink.sbt" % "sbt-purescript" % "0.3.0")

Then:

  * Put your PureScript files (with extension `purs` into `src/main/assets`)
  * Run the `purescript` task
  * Observe the glorious result in `target/web/public/main/js`

Or in a Play Framework project:

  * Put your PureScript files (with extension `purs` into `app/assets`)
  * Run Play
  * Observe the JS file on `http://localhost:9000/main.js`

## Options

There's a `pscOptions` key, with a sequence of parameters given to the `psc` command. You need to scope with within the `Assets` config. For example:

    import PureScriptKeys._
    pscOptions in Assets := Seq("--module", "Main", "--main") 
