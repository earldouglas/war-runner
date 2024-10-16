// Metadata
name := "war-runner"
organization := "com.earldouglas"

// Build
scalacOptions ++= Seq("-feature", "-deprecation")
scalaVersion := "2.12.20" // https://scalameta.org/metals/blog/2023/07/19/silver#support-for-scala-21218

// webapp-runner
lazy val webappRunnerVersion =
  settingKey[String]("webapp-runner version")
webappRunnerVersion := "10.1.28.0"
libraryDependencies += "com.heroku" % "webapp-runner" % webappRunnerVersion.value

// Java-only
Compile / compile / javacOptions ++=
  Seq(
    "-source",
    "11",
    "-target",
    "11",
    "-g:lines"
  )
crossPaths := false // exclude Scala suffix from artifact names
autoScalaLibrary := false // exclude scala-library from dependencies

// Scalafix
semanticdbEnabled := true
semanticdbVersion := scalafixSemanticdb.revision
scalacOptions += "-Ywarn-unused-import"

// Testing
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"
Test / fork := true

// Publish to Sonatype, https://www.scala-sbt.org/release/docs/Using-Sonatype.html
credentials := List(
  Credentials(Path.userHome / ".sbt" / "sonatype_credentials")
)
description := "Launch your webapp without gathering its components"
developers := List(
  Developer(
    id = "earldouglas",
    name = "James Earl Douglas",
    email = "james@earldouglas.com",
    url = url("https://earldouglas.com/")
  )
)
homepage := Some(
  url("https://github.com/earldouglas/war-runner")
)
licenses := List("ISC" -> url("https://opensource.org/licenses/ISC"))
organizationHomepage := Some(url("https://earldouglas.com/"))
organizationName := "James Earl Douglas"
pomIncludeRepository := { _ => false }
publishMavenStyle := true
publishTo := Some(
  "releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
)
scmInfo := Some(
  ScmInfo(
    url("https://github.com/earldouglas/war-runner"),
    "scm:git@github.com:earldouglas/war-runner.git"
  )
)
ThisBuild / versionScheme := Some("semver-spec")
