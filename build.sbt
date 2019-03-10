lazy val circeVersion = "0.10.0"
lazy val catsEffectVersion = "1.2.0"
lazy val scalaTestVersion = "3.0.5"
lazy val scalaMockVersion = "4.1.0"

lazy val scala212 = "2.12.8"
lazy val scala211 = "2.11.12"
lazy val supportedScalaVersions = List(scala212, scala211)

name := "phony"
organization := "com.github.alirezameskin"
homepage := Some(url("https://github.com/alirezameskin/phony"))
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))
bintrayOrganization := Some("meskin")
bintrayRepository := "phony"

version := "0.2-snapshot"
scalaVersion := scala212
crossScalaVersions := supportedScalaVersions

coverageEnabled in ThisBuild := true

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect"   % catsEffectVersion,
  "io.circe"      %% "circe-core"    % circeVersion,
  "io.circe"      %% "circe-generic" % circeVersion,
  "io.circe"      %% "circe-parser"  % circeVersion,
  "org.scalatest" %% "scalatest"     % scalaTestVersion % Test,
  "org.scalamock" %% "scalamock"     % scalaMockVersion % Test,
)
