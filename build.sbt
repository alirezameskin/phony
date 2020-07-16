resolvers += Resolver.sonatypeRepo("releases")

lazy val circeVersion       = "0.12.3"
lazy val catsVersion        = "2.1.1"
lazy val catsEffectVersion  = "2.1.3"
lazy val scalaTestVersion   = "3.1.0"
lazy val scalaMockVersion   = "4.1.0"
lazy val scalaParserVersion = "1.1.2"

lazy val scala213               = "2.13.1"
lazy val scala212               = "2.12.8"
lazy val supportedScalaVersions = List(scala213, scala212)

Global / organization := "com.github.alirezameskin"
Global / homepage := Some(url("https://github.com/alirezameskin/phony"))
Global / licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))
Global / bintrayOrganization := Some("meskin")
Global / scalaVersion := scala213
Global / crossScalaVersions := supportedScalaVersions

lazy val core = (project in file("core"))
  .settings(Settings.commonSettings)
  .settings(
    name := "phony-core",
    bintrayOrganization := Some("meskin"),
    bintrayRepository := "phony",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core"     % catsVersion,
      "io.circe"      %% "circe-core"    % circeVersion,
      "io.circe"      %% "circe-generic" % circeVersion,
      "io.circe"      %% "circe-parser"  % circeVersion,
      "org.scalatest" %% "scalatest"     % scalaTestVersion % Test
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val catsEffect = (project in file("cats-effect"))
  .settings(Settings.commonSettings)
  .settings(
    moduleName := "phony-cats-effect",
    bintrayOrganization := Some("meskin"),
    bintrayRepository := "phony",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % catsEffectVersion
    )
  )
  .aggregate(core)
  .dependsOn(core)
  .disablePlugins(AssemblyPlugin)

lazy val cli = (project in file("cli"))
  .settings(Settings.commonSettings)
  .settings(
    moduleName := "phony-cli",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators" % scalaParserVersion,
      "co.fs2"                 %% "fs2-core"                 % "2.4.0",
      "com.github.scopt"       %% "scopt"                    % "3.7.1",
      "com.monovore"           %% "decline-effect"           % "1.0.0",
      "com.lihaoyi"            %% "fansi"                    % "0.2.7"
    ),
    bintrayUnpublish := {}
  )
  .aggregate(core, catsEffect)
  .dependsOn(core, catsEffect)

lazy val root = (project in file("."))
  .aggregate(core, catsEffect)
  .dependsOn(core, catsEffect)
  .settings(
    moduleName := "phony-root",
    publish := {},
    bintrayUnpublish := {}
  )
  .disablePlugins(AssemblyPlugin)
