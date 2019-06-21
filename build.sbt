resolvers += Resolver.sonatypeRepo("releases")

lazy val circeVersion = "0.10.0"
lazy val catsVersion  = "1.6.1"
lazy val catsEffectVersion = "1.2.0"
lazy val scalaTestVersion = "3.0.5"
lazy val scalaMockVersion = "4.1.0"

lazy val scala212 = "2.12.8"
lazy val scala211 = "2.11.12"
lazy val supportedScalaVersions = List(scala212, scala211)

Global / organization := "com.github.alirezameskin"
Global / homepage := Some(url("https://github.com/alirezameskin/phony"))
Global / licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))
Global / bintrayOrganization := Some("meskin")
Global / version := "0.3.0-snapshot"
Global / scalaVersion := scala212
Global / crossScalaVersions := supportedScalaVersions
Global / coverageEnabled := true

lazy val core = (project in file("core"))
    .settings(
      name := "phony-core",
      bintrayOrganization := Some("meskin"),
      bintrayRepository := "phony",
      libraryDependencies ++= Seq(
        "org.typelevel" %% "cats-core"     % catsVersion,
        "io.circe"      %% "circe-core"    % circeVersion,
        "io.circe"      %% "circe-generic" % circeVersion,
        "io.circe"      %% "circe-parser"  % circeVersion,
        "org.scalatest" %% "scalatest"     % scalaTestVersion % Test,
      ),
      addCompilerPlugin("org.typelevel" % "kind-projector" % "0.10.3" cross CrossVersion.binary)
    )

lazy val catsEffect = (project in file ("cats-effect"))
  .settings(
    moduleName := "phony-cats-effect",
    bintrayOrganization := Some("meskin"),
    bintrayRepository := "phony",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % catsEffectVersion,
    )
  )
  .aggregate(core)
  .dependsOn(core)


lazy val root = (project in file("."))
  .aggregate(core, catsEffect)
  .dependsOn(core, catsEffect)
  .settings(
    moduleName := "phony-root",
    publish := {},
    bintrayUnpublish := {}
  )


