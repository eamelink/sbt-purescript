# sbt-purescript

sbt-purescript is an SBT plugin that compiles [PureScript](http://purescript.org) files to Javascript. It uses and follows the conventions of [sbt-web](https://github.com/sbt/sbt-web).

# Usage

Add the following to the `project/plugins.sbt` of your project:

    resolvers += Resolver.url(
      "eamelinks bintray",
        url("http://dl.bintray.com/eamelink/sbt-plugins"))(
            Resolver.ivyStylePatterns)

    addSbtPlugin("net.eamelink.sbt" % "sbt-purescript" % "0.1.0")

Then:

  * Put your PureScript files (with extension `purs` into `src/main/assets/purescript`)
  * Run the `purescript` task
  * Observe the glorious result in `target/web/public/main/js`