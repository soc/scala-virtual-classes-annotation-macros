import sbt._
import Keys._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "com.github.xmanu.virtual-classes",
    version := "0.1",
    scalacOptions ++= Seq("-deprecation", "-feature"),
    scalaVersion := "2.10.3",
    resolvers += Resolver.sonatypeRepo("snapshots"),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.0-SNAPSHOT" cross CrossVersion.full)
  )
}

object MyBuild extends Build {
  import BuildSettings._

  lazy val root: Project = Project(
    "root",
    file("."),
    settings = buildSettings ++ Seq(
      run <<= run in Compile in core
    )
  ) aggregate(macros, core)

  lazy val macros: Project = Project(
    "virtual-classes-macros",
    file("macros"),
    settings = buildSettings ++ Seq(
      libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _),
      libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-compiler" % _),
      libraryDependencies  += ("org.scalamacros" %% "quasiquotes" % "2.0.0-SNAPSHOT" cross CrossVersion.full))
  )

  lazy val core: Project = Project(
    "virtual-classes-core",
    file("core"),
    settings = buildSettings
  ) dependsOn(macros)
}